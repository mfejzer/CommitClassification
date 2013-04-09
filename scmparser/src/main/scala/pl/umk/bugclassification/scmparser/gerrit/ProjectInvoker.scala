package pl.umk.bugclassification.scmparser.gerrit
import scala.actors.Actor
import pl.umk.bugclassification.scmparser.Classify
import pl.umk.bugclassification.scmparser.Learn
import pl.umk.bugclassification.scmparser.InvokerOnDirectory

trait ProjectInvoker extends Actor with InvokerOnDirectory{
  protected def resetRepo: Unit

  protected def resetRepo(branch: String): Unit

  protected def fetch: Unit

  protected def fetchAndCheckoutFromGerrit(ref: String)

  protected def learn
  
  protected def classify(ref: String, sha1: String): Boolean
  
  protected def send(sha1: String, isCommitClassifiedBuggy: Boolean): Unit

  def act() {
    while (true) {
      receive {
        case Learn("master") => {
          resetRepo
          learn
        }
        case Learn(branch) => {
          resetRepo(branch)
          learn
        }
        case Classify(ref, sha1) => {
          fetchAndCheckoutFromGerrit(ref)
          val isCommitClassifiedBuggy = classify(ref, sha1)
          send(sha1, isCommitClassifiedBuggy)
        }
      }
    }
  }
}
