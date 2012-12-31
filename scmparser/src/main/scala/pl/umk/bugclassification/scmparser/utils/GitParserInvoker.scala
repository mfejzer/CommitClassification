package pl.umk.bugclassification.scmparser.utils
import scala.sys.process.Process
import scala.sys.process.ProcessIO
import pl.umk.bugclassification.scmparser.utils.parsers.CommitParser
import pl.umk.bugclassification.scmparser.utils.parsers.BlameParser

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

  def extractLog(): String = {
    createProcessBuilder(GitCommand.logNoMerges()).lines.mkString("\n")
  }

  def listLoggedCommits(): List[Commit] = {
    CommitParser.commitsFromLog(extractLog())
  }

  def findCausesForFix(fix: Commit): List[(Commit, Commit)] = {
    val filesWithDiffs = fix.filenames.map(file => extractDiffFromCommitForFile(fix, file))
    val blamesPerFile = fix.filenames.map(file => blameOnCommitParentForFile(fix, file))
    //Not ready
    List((fix, fix)).toList
  }

  def extractDiffFromCommitForFile(commit: Commit, file: String): (String, List[String]) = {
    (file,
      filterRemovedLines(createProcessBuilder(GitCommand.diffOnFileWithParent(commit, file)).lines.toList))
  }

  def filterRemovedLines(fileContent: List[String]): List[String] = {
    fileContent.filter(x => { x.startsWith("-") && (!x.contains("---")) }).map(x => x.drop(1))
  }

  def blameOnCommitParentForFile(commit: Commit, file: String): List[Blame] = {
    BlameParser.blamesFromInput(extractBlame(commit, file))
  }

  def extractBlame(commit: Commit, file: String): String = {
//    println(GitCommand.blameOnFileWithParent(commit, file))
    createProcessBuilder(GitCommand.blameOnFileWithParent(commit, file)).lines.mkString("\n")+"\n"
  }

}