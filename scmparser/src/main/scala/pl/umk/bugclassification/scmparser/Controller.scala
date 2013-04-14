package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.training.ModelDAO
import scala.actors.Actor
import pl.umk.bugclassification.scmparser.gerrit.ProjectInvoker

class Controller(private val port: Int, private val hostname: String,
  private val user: String, private val directory: String,
  private val modelDao: ModelDAO) extends Actor {

  var projectInvokers = new ProjectPreparer(port, hostname, user, directory, modelDao).projectInvokers
  start

  def act() = {
    loop {
      receive {
        case PreprareAllProjects => {
          projectInvokers = new ProjectPreparer(port, hostname, user, directory, modelDao).projectInvokers
        }
        case LearnOnAllProjects => {
          val learner = new Learner(projectInvokers)
          learner.trainAll
        }
        case LearnOnProject(project: String) => {
          val learner = new Learner(projectInvokers)
          learner.trainOnProject(project)
        }
        case LearnOnProjectAndBranch(project: String, branch: String) => {
          val learner = new Learner(projectInvokers)
          learner.trainOnProject(project, branch)
        }
        case ClassifyOnProject(project: String, ref: String, sha1: String) => {
          projectInvokers.get(project).foreach(projectInvoker => projectInvoker ! Classify(ref, sha1))
        }
      }
    }
  }

}