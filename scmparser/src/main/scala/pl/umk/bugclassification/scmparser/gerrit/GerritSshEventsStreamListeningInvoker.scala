package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.ListeningInvoker

class GerritSshEventsStreamListeningInvoker(private val port: Int,
  private val hostname: String) extends ListeningInvoker {
  runProcess(GerritSshStreamEventsCommand(port, hostname))
  
  def callback(line: String): Unit = {println(line)}

}