package pl.umk.bugclassification.scmparser

import pl.umk.bugclassification.scmparser.gerrit.GerritSshEventsStreamListeningInvoker
import pl.umk.bugclassification.scmparser.messages.ClassifyOnProject

import org.slf4j.LoggerFactory

class Worker(private val port: Int, private val hostname: String, private val controller: Controller) {
  val log = LoggerFactory.getLogger(classOf[Worker])
  private val listeningInvoker = new GerritSshEventsStreamListeningInvoker(port, hostname,
    project => ref => sha1 => {
      log.info("Received " + project + " " + ref + " " + sha1)
      controller.self ! ClassifyOnProject(project, ref, sha1)
    })
}