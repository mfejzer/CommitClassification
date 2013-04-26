package pl.umk.bugclassification.scmparser.git

import scala.collection.JavaConversions.seqAsJavaList

import pl.umk.bugclassification.scmparser.git.parsers.results.Commit
import pl.umk.bugclassification.scmparser.Command

trait GitCommand extends Command {

}

case object GitFetchCommand extends GitCommand {
  def command = List("git", "fetch")
}

case object GitResetHardOnOriginMasterCommand extends GitCommand {
  def command = List("git", "reset", "--hard", "origin/master")
}

case class GitResetHardOnBranchCommand(private val branch: String) extends GitCommand {
  def command = List("git", "reset", "--hard", branch)
}

case object GitLogOnelineCommand extends GitCommand {
  def command = List("git", "log", "--oneline")
}

case object GitLogNoMergesCommand extends GitCommand {
  def command = List("git", "log", "--name-only","--no-merges")
}

case class GitShowCommitCommand(sha1: String) extends GitCommand {
  def command = List("git", "show", sha1)
}

case class GitDiffCommand(firstSha1: String, secondSha1: String) extends GitCommand {
  def command = List("git", "diff", firstSha1, secondSha1)
}

case class GitDiffOnFileWithParentCommand(commit: Commit, file: String) extends GitCommand {
  def command = List("git", "diff", commit.sha1, commit.parent, file)
}

case class GitBlameOnFileWithParentCommand(commit: Commit, file: String) extends GitCommand {
  def command = List("git", "blame", "-l", "-f", commit.parent, file)
}



