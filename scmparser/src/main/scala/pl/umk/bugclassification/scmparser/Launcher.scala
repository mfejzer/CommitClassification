package pl.umk.bugclassification.scmparser

import akka.actor.ActorSystem
import pl.umk.bugclassification.scmparser.messages.{LearnOnAllProjects, PreprareAllProjects, PreprareMissingProjects}
import pl.umk.bugclassification.scmparser.training.ModelDAOImpl

import scala.concurrent.duration._

object Launcher {

  def start(host: String, port: Int, user: String, directory: String, repeatLearnAfterHours: Int) {
    val system = ActorSystem("scmparser")
    val controller = new Controller(port, host, user, directory, new ModelDAOImpl)
    val worker = new Worker(port, host, controller)
    controller.self ! PreprareAllProjects
    controller.self ! LearnOnAllProjects
    val when = repeatLearnAfterHours * 3600 * 1000

    import system.dispatcher

    system.scheduler.schedule(50 milliseconds, when milliseconds)({
      controller.self ! PreprareMissingProjects
      controller.self ! LearnOnAllProjects
    })
  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("scmparser")
    val controller = new Controller(29418, "machina", "mfejzer", "/home/mfejzer/src/bonus", new ModelDAOImpl)
    val worker = new Worker(29418, "machina", controller)
    controller.self ! PreprareAllProjects
  }

}