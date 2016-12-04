package pl.umk.bugclassification

import org.rogach.scallop._
import pl.umk.bugclassification.scmparser.Launcher

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val user = opt[String]("user", required = true)
  val port = opt[Int]("port", required = true)
  val hostname = opt[String]("hostname", required = true)
  val directory = opt[String]("directory", required = true)
  val repeatLearnAfterHours = opt[Int]("repeatLearnAfterHours", required = true)
}

object App {

  def main(args: Array[String]) {
    val conf = new Conf(args)
    val port = conf.port.apply()
    val hostname = conf.hostname.apply()
    val user = conf.user.apply()
    val directory = conf.directory.apply()
    val repeatLearnAfterHours = conf.repeatLearnAfterHours.apply()
    Launcher.start(hostname, port, user, directory, repeatLearnAfterHours)
  }

}




