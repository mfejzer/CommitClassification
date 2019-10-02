package pl.umk.bugclassification.scmparser

import akka.actor.Actor
import pl.umk.bugclassification.scmparser.messages._
import pl.umk.bugclassification.scmparser.training.ModelDAO

class Controller(private val port: Int, private val hostname: String,
                 private val user: String, private val directory: String,
                 private val historyLimit: Int, private val modelDao: ModelDAO) extends Actor {

  var projectInvokers = new ProjectPreparer(port, hostname, user, directory, historyLimit, modelDao).allProjectInvokers

  def receive = {
    case PreprareAllProjects => {
      projectInvokers = new ProjectPreparer(port, hostname, user, directory, historyLimit, modelDao).allProjectInvokers
    }
    case PreprareMissingProjects => {
      projectInvokers.++(new ProjectPreparer(port, hostname, user, directory, historyLimit, modelDao).missingProjectInvokers)
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
      projectInvokers.get(project).foreach(projectInvoker => projectInvoker.self ! Classify(ref, sha1))
    }
  }

}