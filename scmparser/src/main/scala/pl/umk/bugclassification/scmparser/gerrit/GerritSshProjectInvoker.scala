package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.git.GitFetchCommand
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.git.GitResetHardOnBranchCommand
import pl.umk.bugclassification.scmparser.git.GitResetHardOnOriginMasterCommand
import pl.umk.bugclassification.scmparser.training.Classificator
import pl.umk.bugclassification.scmparser.training.ModelDAO
import pl.umk.bugclassification.scmparser.training.Trainer
import pl.umk.bugclassification.scmparser.training.WekaWrapperBuilder

class GerritSshProjectInvoker(private val port: Int, private val hostname: String,
  private val user: String, val projectName: String, private val directory: String,
  private val modelDao: ModelDAO) extends ProjectInvoker {

  private val commentSender = new GerritSshCommentOnPatchSetInvoker(port, hostname)

  start

  def dirUrl(): String = { directory }

  protected def resetRepo: Unit = {
    fetch
    createProcessBuilder(GitResetHardOnOriginMasterCommand) !
  }

  protected def resetRepo(branch: String): Unit = {
    fetch
    createProcessBuilder(GitResetHardOnBranchCommand(branch)) !
  }

  protected def fetch: Unit = {
    createProcessBuilder(GitFetchCommand) !
  }

  protected def fetchAndCheckoutFromGerrit(ref: String): Unit = {
    val fetch = createProcessBuilder(GitFetchFromGerritPatchSetCommand(port, hostname, user, projectName, ref))
    val checkout = createProcessBuilder(GitCheckoutPatchSetCommand)
    fetch #&& checkout !
  }

  protected def learn: Unit = {
    val parserInvoker = new GitParserInvoker(projectName, directory)
    val trainer = new Trainer(parserInvoker, WekaWrapperBuilder.getSvmBuilder, modelDao)
    trainer.measurePerformance
  }

  protected def classify(ref: String, sha1: String): Boolean = {
    val parserInvoker = new GitParserInvoker(projectName, directory)
    val diff = parserInvoker.showCommit(sha1)
    val classificator = new Classificator(modelDao)
    classificator.classificateCommit(projectName, diff)
  }

  protected def send(sha1: String, isCommitClassifiedBuggy: Boolean): Unit = {
    val message = if (isCommitClassifiedBuggy) """'"Commit classified buggy"'""" else """'"Commit classified clean"'"""
    commentSender.comment(sha1, message)
  }

}