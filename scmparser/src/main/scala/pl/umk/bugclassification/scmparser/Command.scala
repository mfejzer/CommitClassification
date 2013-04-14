package pl.umk.bugclassification.scmparser

import scala.collection.JavaConversions.seqAsJavaList

trait Command {
  def command: java.util.List[String]
}

case class RmCommand(projectName:String) extends Command {
  def command = List("rm", "-rf", projectName)
}