package pl.umk.bugclassification.scmparser.utils
import scala.sys.process.Process
import scala.sys.process.ProcessIO

class GitParserInvoker(private val repoLocationUrl: String) {
  private def createProcessBuilder(params: java.util.List[String]): scala.sys.process.ProcessBuilder = {
    val pb = Process((new java.lang.ProcessBuilder(params))
      directory new java.io.File(repoLocationUrl))
    return pb
  }

  def listLoggedCommitsSHA1s(): List[String] = {
    createProcessBuilder(GitCommand.logOneline()).lines.toList.map(x => { (x.split(" ")(0)) })
  }

  def showCommit(sha1: String): List[String] = {
    createProcessBuilder(GitCommand.showCommit(sha1)).lines.toList
  }

  def showDiff(commit1: String, commit2: String): List[String] = {
    createProcessBuilder(GitCommand.diff(commit1, commit2)).lines.toList
  }

  def extractRawLog(): String = {
    createProcessBuilder(GitCommand.logNoMerges()).lines.mkString("\n")
  }

  def listLoggedCommits(): List[Commit] = {
    CommitParser.commitsFromLog(extractRawLog())
  }

  def findCausesForFix(fix: Commit): List[(Commit, Commit)] = {
    fix.filenames.map(x => (x, createProcessBuilder(GitCommand.diffOnFileWithParent(fix, x)).lines.toList))

    List((fix, fix)).toList
  }

}