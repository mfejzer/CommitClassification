package pl.umk.bugclassification.scmparser
import scala.sys.process.Process
import scala.sys.process.ProcessIO


trait ListeningInvoker {

  def callback(line: String): Unit

  def runProcess(command: Command) {
    val pb = Process(new java.lang.ProcessBuilder(command.command))
    val pio = new ProcessIO(_ => (),
      stdout => scala.io.Source.fromInputStream(stdout)
        .getLines.foreach(callback),
      _ => ())
    pb.run(pio)
  }

}