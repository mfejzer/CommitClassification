package pl.umk.bugclassification.scmparser

import scala.actors.Actor
import scala.actors.TIMEOUT

import pl.umk.bugclassification.scmparser.messages.LearnOnAllProjects
import pl.umk.bugclassification.scmparser.messages.PreprareAllProjects
import pl.umk.bugclassification.scmparser.messages.PreprareMissingProjects
import pl.umk.bugclassification.scmparser.training.ModelDAOImpl


object Launcher extends LoggingConf {

  def start(host: String, port: Int, user: String, directory: String, repeatLearnAfterHours: Int) {
    val controller = new Controller(port, host, user, directory, new ModelDAOImpl)
    val worker = new Worker(port, host, controller)
    controller ! PreprareAllProjects
    controller ! LearnOnAllProjects
    val when = repeatLearnAfterHours * 3600 * 1000
    val sched = scheduler(when) {
      controller ! PreprareMissingProjects
      controller ! LearnOnAllProjects
    }
  }

  def main(args: Array[String]): Unit = {
    val controller = new Controller(29418, "machina", "mfejzer", "/home/mfejzer/src/bonus", new ModelDAOImpl)
    val worker = new Worker(29418, "machina", controller)
    //    controller ! LearnOnProject("tmp")
    controller ! PreprareAllProjects
    //    controller ! LearnOnAllProjects
//    controller ! LearnOnProject("egit")

  }

  def scheduler(time: Long)(f: => Unit) = {
    def fixedRateLoop {
      Actor.reactWithin(time) {
        case TIMEOUT => f; fixedRateLoop
        case 'stop =>
      }
    }
    Actor.actor(fixedRateLoop)
  }
}