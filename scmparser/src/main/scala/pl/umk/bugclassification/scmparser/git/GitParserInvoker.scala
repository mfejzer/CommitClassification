package pl.umk.bugclassification.scmparser.git
import scala.sys.process.Process
import scala.sys.process.ProcessIO
import pl.umk.bugclassification.scmparser.git.parsers.CommitParser
import pl.umk.bugclassification.scmparser.git.parsers.BlameParser
import pl.umk.bugclassification.scmparser.git.parsers.results.Commit
import pl.umk.bugclassification.scmparser.git.parsers.results.Blame

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

  def findCausesForFix(fix: Commit): List[(Commit, String)] = {
    val filesWithDiffs = fix.filenames.map(file => extractDiffFromCommitForFile(fix, file)) //list of (file,removed lines) for commit fix
    val blamesPerFile = fix.filenames.map(file => blameOnCommitParentForFile(fix, file))
    val combined = filesWithDiffs.zip(blamesPerFile)
    val result = combined.
      map(pair => process(pair)).
      flatten.removeDuplicates.
      map(bugIntroductingSha1 => (fix, bugIntroductingSha1))

    result
  }

  def process(pair: (List[String], List[Blame])): List[String] = {
    val diffs = pair._1
    val blames = pair._2
    diffs.map(line => { blames.filter(blame => blame.line == line).map(blame => blame.sha1) }).flatten.removeDuplicates
  }

  def extractDiffFromCommitForFile(commit: Commit, file: String): List[String] = {
    filterRemovedLines(createProcessBuilder(GitCommand.diffOnFileWithParent(commit, file)).lines.toList)
  }

  def filterRemovedLines(fileContent: List[String]): List[String] = {
    fileContent.filter(x => { x.startsWith("-") && (!x.contains("---")) }).map(x => x.drop(1))
  }

  def blameOnCommitParentForFile(commit: Commit, file: String): List[Blame] = {
    BlameParser.blamesFromInput(extractBlame(commit, file))
  }

  def extractBlame(commit: Commit, file: String): String = {
    //    println(GitCommand.blameOnFileWithParent(commit, file))
    createProcessBuilder(GitCommand.blameOnFileWithParent(commit, file)).lines.mkString("\n") + "\n"
  }

}