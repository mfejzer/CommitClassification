package pl.umk.bugclassification.scmparser.gerrit

import akka.actor.{Actor, ActorLogging}
import pl.umk.bugclassification.scmparser.invokers.InvokerOnDirectory
import pl.umk.bugclassification.scmparser.messages.{Classify, Learn}

trait ProjectInvoker extends Actor with ActorLogging with InvokerOnDirectory {
  protected def resetRepo: Unit

  protected def resetRepo(branch: String): Unit

  protected def fetch: Unit

  protected def fetchAndCheckoutFromGerrit(ref: String)

  protected def learn

  protected def classify(ref: String, sha1: String): Boolean

  protected def send(sha1: String, isCommitClassifiedBuggy: Boolean): Unit

  def receive = {
    case Learn("master") => {
      resetRepo
      learn
    }
    case Learn(branch) => {
      resetRepo(branch)
      learn
    }
    case Classify(ref, sha1) => {
      log.info("Classify " + ref + " " + sha1)
      fetchAndCheckoutFromGerrit(ref)
      val isCommitClassifiedBuggy = classify(ref, sha1)
      send(sha1, isCommitClassifiedBuggy)
    }
  }
}
