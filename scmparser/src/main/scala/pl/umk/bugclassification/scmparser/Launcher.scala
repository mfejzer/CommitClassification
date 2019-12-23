package pl.umk.bugclassification.scmparser

import akka.actor.ActorSystem
import org.rogach.scallop.ScallopConf
import pl.umk.bugclassification.scmparser.messages.{LearnOnAllProjects, PreprareAllProjects, PreprareMissingProjects}
import pl.umk.bugclassification.scmparser.training.ModelDAOImpl

import scala.concurrent.duration._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val hostname = opt[String]("hostname", required = false)
  val port = opt[Int]("port", required = false)
  val user = opt[String]("user", required = true)
  val directory = opt[String]("directory", required = true)
  val repeatLearnAfterHours = opt[Int]("repeatLearnAfterHours", required = false)
  val historyLimit = opt[Int]("historyLimit", required = false)
}

object Launcher {

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)
    conf.verify()

    val hostname = conf.hostname.getOrElse("localhost")
    val port = conf.port.getOrElse(29418)
    val user = conf.user.apply()
    val directory = conf.directory.apply()
    val repeatLearnAfterHours = conf.repeatLearnAfterHours.getOrElse(24)
    val historyLimit = conf.historyLimit.getOrElse(2000)

    Launcher.start(hostname, port, user, directory, repeatLearnAfterHours, historyLimit)
  }

  def start(host: String, port: Int, user: String, directory: String, repeatLearnAfterHours: Int, historyLimit: Int) {
    val system = ActorSystem("scmparser")
    val controller = new Controller(port, host, user, directory, historyLimit, new ModelDAOImpl)
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

}