package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.training.ModelDAO
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.training.Classificator
import pl.umk.bugclassification.scmparser.gerrit.GerritSshCommentOnPatchSetInvoker

class Worker(private val port: Int, private val hostname: String,
  private val parserInvokers: Map[String, GitParserInvoker], private val modelDao: ModelDAO) {
  private val classificator = new Classificator(modelDao)
  private val commentSender = new GerritSshCommentOnPatchSetInvoker(port, hostname)

  def f(project: String, ref: String, sha1: String) {
    val parserInvoker = parserInvokers.get(project).get//change to foreach
    parserInvoker.fetchAndCheckoutFromGerrit(ref)
    val isCommitClassifiedBuggy = classificator.classificateCommit(project, parserInvoker.showCommit(sha1))
    parserInvoker.fetch
    parserInvoker.resetRepo
    commentSender.comment(sha1,
      if (isCommitClassifiedBuggy) "Commit classified buggy" else "Commit classified clean")
  }
}