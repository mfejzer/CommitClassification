package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.gerrit.ProjectInvoker
import pl.umk.bugclassification.scmparser.gerrit.GerritSshEventsStreamListeningInvoker

class Worker(private val port: Int, private val hostname: String, private val controller: Controller) {
  val listeningInvoker = new GerritSshEventsStreamListeningInvoker(port, hostname,
    project => ref => sha1 => {
      println(sha1)
      controller ! ClassifyOnProject(project, ref, sha1)
    })
}