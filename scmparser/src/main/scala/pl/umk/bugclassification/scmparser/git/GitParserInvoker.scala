package pl.umk.bugclassification.scmparser.git

import org.slf4j.LoggerFactory
import pl.umk.bugclassification.scmparser.git.parsers.results.{Blame, Commit}
import pl.umk.bugclassification.scmparser.git.parsers.{BlameParser, CommitParser}

class GitParserInvoker(private val projectName: String,
                       private val repoLocationUrl: String,
                       private val historyLimit: Int) extends ParserInvoker {

  val log = LoggerFactory.getLogger(classOf[GitParserInvoker])

  def dirUrl: String = repoLocationUrl

  def getProjectName = projectName

  def listLoggedCommitsSHA1s(): List[String] = {
    createProcessBuilder(GitLogOnelineCommand).lines.toList.map(x => {
      x.split(" ")(0)
    })
  }

  def showCommit(sha1: String): List[String] = {
    createProcessBuilder(GitShowCommitCommand(sha1)).lines.toList
  }

  def showDiff(commit1: String, commit2: String): List[String] = {
    createProcessBuilder(GitDiffCommand(commit1, commit2)).lines.toList
  }

  private def extractLog(): String = {
    createProcessBuilder(GitLogNoMergesCommand(historyLimit)).lines.mkString("\n") + "\n"
  }

  def listLoggedCommits(): List[Commit] = {
    CommitParser.commitsFromLog(extractLog())
  }

  def findCausesForFix(fix: Commit): List[(Commit, String)] = {
    val filesWithDiffs = fix.filenames.map(file => extractDiffFromCommitForFile(fix, file))
    //list of (file,removed lines) for commit fix
    val blamesPerFile = fix.filenames.map(file => blameOnCommitParentForFile(fix, file))
    val combined = filesWithDiffs.zip(blamesPerFile)
    val result = combined.flatMap(pair => process(pair)).distinct.
      map(bugIntroductingSha1 => (fix, bugIntroductingSha1))

    result
  }

  private def process(pair: (List[String], List[Blame])): List[String] = {
    val diffs = pair._1
    val blames = pair._2
    diffs.flatMap(line => {
      blames.filter(blame => blame.line == line).
        map(blame => blame.sha1)
    }).distinct
  }

  def extractDiffFromCommitForFile(commit: Commit, file: String): List[String] = {
    log.debug("extractDiffFromCommitForFile " + commit.sha1 + " " + file)
    filterRemovedLines(createProcessBuilder(GitDiffOnFileWithParentCommand(commit, file)).lineStream.toList)
  }

  private def filterRemovedLines(fileContent: List[String]): List[String] = {
    fileContent.filter(x => {
      x.startsWith("-") && (!x.contains("---"))
    }).map(x => x.drop(1))
  }

  def blameOnCommitParentForFile(commit: Commit, file: String): List[Blame] = {
    extractBlame(commit, file).flatMap(x => BlameParser.blamesFromInput(x)).getOrElse(List[Blame]())
  }

  def extractBlame(commit: Commit, file: String): Option[String] = {
    log.debug("extractBlame " + commit.sha1 + " " + file)
    var tmp: Option[String] = None
    try {
      val result = createProcessBuilder(GitBlameOnFileWithParentCommand(commit, file)).lineStream.mkString("\n") + "\n"
      tmp = Some(result)
    } catch {
      case e: Exception => log.error(e.getMessage)
    }
    tmp
  }

}