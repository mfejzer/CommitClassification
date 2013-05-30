package pl.umk.bugclassification.scmparser.gerrit

import scala.collection.JavaConversions.seqAsJavaList

import pl.umk.bugclassification.scmparser.git.GitCommand
import pl.umk.bugclassification.scmparser.invokers.Command

trait GerritSshCommand extends Command {

}

case class GerritSshStreamEventsCommand(private val port: Int, private val hostname: String)
  extends GerritSshCommand {
  def command = List("ssh", "-p", port.toString(), hostname, "gerrit", "stream-events")
}

case class GerritSshCommentOnPatshSetCommand(private val port: Int, private val hostname: String,
  private val sha1: String, private val message: String) extends GerritSshCommand {
  def command =
    List("ssh", "-p", port.toString(), hostname, "gerrit", "review", "-m", message, "--code-review", "0", sha1)
}

case class GerritSshLsProjectsCommand(private val port: Int, private val hostname: String)
  extends GerritSshCommand {
  def command = List("ssh", "-p", port.toString(), hostname, "gerrit", "ls-projects")
}

case class GitCloneProjectFromGerritCommand(private val port: Int, private val hostname: String,
  private val user: String, private val projectName: String) extends GitCommand with GerritSshCommand {
  def command =
    List("git", "clone", "ssh://" + user + "@" + hostname + ":" + port.toString() + "/" + projectName)
}

case class GitFetchFromGerritPatchSet(private val port: Int, private val hostname: String,
  private val user: String, private val projectName: String, private val ref: String)
  extends GitCommand with GerritSshCommand {
  def command =
    List("git", "fetch", "ssh://" + user + "@" + hostname + ":" + port + "/" + projectName, ref)
}

case object GitCheckoutPatchSet extends GitCommand {
  def command = List("git", "checkout", "FETCH_HEAD")
}