package pl.umk.bugclassification.scmparser
import com.codahale.logula.Logging

import pl.umk.bugclassification.scmparser.gerrit.GerritSshEventsStreamListeningInvoker
import pl.umk.bugclassification.scmparser.messages.ClassifyOnProject

class Worker(private val port: Int, private val hostname: String, private val controller: Controller) extends Logging {
  private val listeningInvoker = new GerritSshEventsStreamListeningInvoker(port, hostname,
    project => ref => sha1 => {
      log.info("Received " + project + " " + ref + " " + sha1)
      controller ! ClassifyOnProject(project, ref, sha1)
    })
}