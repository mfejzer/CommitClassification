package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.gerrit.ProjectInvoker

class Worker(private val projectInvokers: Map[String, ProjectInvoker]) {
  def f(project: String, ref: String, sha1: String) {
    projectInvokers.get(project).foreach(projectInvoker => projectInvoker ! Classify(ref, sha1))
  }
}