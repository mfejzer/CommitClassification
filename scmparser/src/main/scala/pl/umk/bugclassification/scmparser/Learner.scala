package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.training.ModelDAO
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.training.Trainer
import pl.umk.bugclassification.scmparser.gerrit.ProjectInvoker

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