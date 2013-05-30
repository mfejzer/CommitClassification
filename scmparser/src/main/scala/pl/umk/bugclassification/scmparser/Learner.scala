package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.gerrit.ProjectInvoker
import pl.umk.bugclassification.scmparser.messages.Learn

class Learner(private val projectInvokers: Map[String, ProjectInvoker]) {

  def trainAll = {
    projectInvokers.values.par.foreach(projectInvoker => projectInvoker ! Learn("master"))
  }

  def trainOnProject(project: String) = {
    projectInvokers.get(project).foreach(projectInvoker => projectInvoker ! Learn("master"))
  }

  def trainOnProject(project: String, branch: String) = {
    projectInvokers.get(project).foreach(projectInvoker => projectInvoker ! Learn(branch))
  }
}