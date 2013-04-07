package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.ListeningInvoker
import pl.umk.bugclassification.scmparser.gerrit.parsers.JsonGerritEventStreamParser

class GerritSshEventsStreamListeningInvoker(private val port: Int, private val hostname: String,
  private val executeOnCreatePatchSet: String => String => String => Unit) extends ListeningInvoker {
  runProcess(GerritSshStreamEventsCommand(port, hostname))

  def callback(line: String): Unit = {
    val event = JsonGerritEventStreamParser.processEvent(line)
    if (event.isDefined) {
      val project = event.get.change.project
      val ref = event.get.patchSet.ref
      val sha1 = event.get.patchSet.revision
      executeOnCreatePatchSet(project)(ref)(sha1)
    } else {
      println(line)
    }
  }

}