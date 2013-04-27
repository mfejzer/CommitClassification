package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.gerrit.ProjectInvoker
import pl.umk.bugclassification.scmparser.gerrit.GerritSshEventsStreamListeningInvoker
import com.codahale.logula.Logging

class Worker(private val port: Int, private val hostname: String, private val controller: Controller) extends Logging {
  val listeningInvoker = new GerritSshEventsStreamListeningInvoker(port, hostname,
    project => ref => sha1 => {
      log.info("Received " + project + " " + ref + " " + sha1)
      controller ! ClassifyOnProject(project, ref, sha1)
    })
}