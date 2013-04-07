package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.gerrit.GerritSshProjectsInvoker
import java.io.File
import scala.io.Source

class ProjectPreparer(private val port: Int, private val hostname: String,
  private val user: String, private val directory: String) {
  private val gerritSshProjectsInvoker = new GerritSshProjectsInvoker(port, hostname, user, directory)
  private val currentProjects = getProjectsFromDirectory

  val parserInvokers = if (currentProjects.isEmpty) {
    gerritSshProjectsInvoker.cloneAllProjects
  } else {
    gerritSshProjectsInvoker.cloneMissingProjects(currentProjects)
  }

  private def getProjectsFromDirectory: List[String] = {
    val listedFiles = new File(directory).listFiles()
    listedFiles.map(file => file.getName()).toList

  }
}