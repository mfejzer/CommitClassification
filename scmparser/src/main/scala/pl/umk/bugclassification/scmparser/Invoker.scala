package pl.umk.bugclassification.scmparser
import scala.sys.process.Process

trait Invoker {
  def runProcess(command: Command) = {
    val pb = Process(new java.lang.ProcessBuilder(command.command))
    pb.run()
  }
}