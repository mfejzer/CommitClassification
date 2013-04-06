package pl.umk.bugclassification.scmparser
import scala.sys.process.Process
import scala.sys.process.ProcessIO
import scala.concurrent.SyncVar
import java.io.OutputStream

trait ListeningInvoker {

  def callback(line: String): Unit
  private val inputStream = new SyncVar[OutputStream];

  def runProcess(command: Command) {
    val pb = Process(new java.lang.ProcessBuilder(command.command))
    val pio = new ProcessIO(stdin => inputStream.put(stdin),
      stdout => scala.io.Source.fromInputStream(stdout)
        .getLines.foreach(callback),
      stderr => scala.io.Source.fromInputStream(stderr)
        .getLines.foreach(println))
    pb.run(pio)
  }

  def writeToInput(s: String): Unit = synchronized {
    inputStream.get.write((s + "\n").getBytes)
  }
}