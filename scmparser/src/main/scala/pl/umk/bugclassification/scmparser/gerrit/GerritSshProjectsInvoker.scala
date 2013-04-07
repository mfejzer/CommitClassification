package pl.umk.bugclassification.scmparser.gerrit
import scala.collection.mutable.MapBuilder

import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.InvokerOnDirectory

class GerritSshProjectsInvoker(private val port: Int, private val hostname: String,
  private val user: String, private val directory: String) extends InvokerOnDirectory {
  def dirUrl = directory

  private def getAllProjects: List[String] = {
    createProcessBuilder(GerritSshLsProjectsCommand(port, hostname)).lines.toList
  }

  private def cloneAndCreateParserInvoker(projects: List[String]): Map[String, GitParserInvoker] = {
    projects.foreach(projectName =>
      createProcessBuilder(GitCloneProjectFromGerritCommand(port, hostname, projectName)).
        run())

    createParserInvokers(projects)
  }

  private def createParserInvokers(projects: List[String]): Map[String, GitParserInvoker] = {
    val mapBuilder = new MapBuilder[String, GitParserInvoker, Map[String, GitParserInvoker]](Map.empty)
    projects.foreach(projectName =>
      mapBuilder += projectName -> new GitParserInvoker(port, hostname, user, projectName, directory + "/" + projectName))

    mapBuilder.result
  }

  def cloneAllProjects: Map[String, GitParserInvoker] = {
    val projects = getAllProjects
    cloneAndCreateParserInvoker(projects)
  }

  def cloneMissingProjects(currentProjects: List[String]): Map[String, GitParserInvoker] = {
    val projects = getAllProjects.filterNot(projectName => currentProjects.contains(projectName))
    createParserInvokers(currentProjects) ++ cloneAndCreateParserInvoker(projects)
  }

}