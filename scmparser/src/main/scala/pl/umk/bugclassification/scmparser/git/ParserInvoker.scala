package pl.umk.bugclassification.scmparser.git
import pl.umk.bugclassification.scmparser.git.parsers.results.Blame
import pl.umk.bugclassification.scmparser.git.parsers.results.Commit
import pl.umk.bugclassification.scmparser.InvokerOnDirectory

trait ParserInvoker extends InvokerOnDirectory {
  def getProjectName: String
  def listLoggedCommitsSHA1s(): List[String]
  def showCommit(sha1: String): List[String]
  def showDiff(commit1: String, commit2: String): List[String]
  def listLoggedCommits(): List[Commit]
  def findCausesForFix(fix: Commit): List[(Commit, String)]
  def extractDiffFromCommitForFile(commit: Commit, file: String): List[String]
  def blameOnCommitParentForFile(commit: Commit, file: String): List[Blame]
  def extractBlame(commit: Commit, file: String): String
}