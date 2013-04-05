package pl.umk.bugclassification.scmparser
import scala.sys.process.Process

trait InvokerOnDirectory {

  def dirUrl: String

  protected def createProcessBuilder(command: Command): scala.sys.process.ProcessBuilder = {
    val pb = Process((new java.lang.ProcessBuilder(command.command))
      directory new java.io.File(dirUrl))
    return pb
  }
}