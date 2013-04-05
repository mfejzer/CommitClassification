package pl.umk.bugclassification.scmparser.gerrit

import scala.collection.JavaConversions._
import pl.umk.bugclassification.scmparser.Command
import pl.umk.bugclassification.scmparser.git.GitCommand

trait GerritSshCommand extends Command {

}

case class GerritSshStreamEventsCommand(private val port: Int, private val hostname: String)
  extends GerritSshCommand {
  def command = List("ssh", "-p", port.toString(), hostname, "gerrit", "stream-events")
}

case class GerritSshCommentOnPatshSetCommand(private val port: Int, private val hostname: String,
  private val sha1: String, private val message: String) extends GerritSshCommand {
  def command = List("ssh", "-p", port.toString(), hostname, "gerrit", "review", "-m", message, sha1)
}

case class GerritSshLsProjectsCommand(private val port: Int, private val hostname: String)
  extends GerritSshCommand {
  def command = List("ssh", "-p", port.toString(), hostname, "gerrit", "ls-projects")
}

case class GitCloneProjectFromGerritCommand(private val port: Int, private val hostname: String,
  private val projectName: String) extends GitCommand with GerritSshCommand {
  def command = List("git", "clone", "--bare",
    "ssh:" + hostname + ":" + port.toString() + "/" + projectName + ".git",
    projectName + ".git") //git clone --bare "ssh://review.example.com:29418/$p.git" "$p.git"
}