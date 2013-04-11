package pl.umk.bugclassification.scmparser.gerrit
import scala.collection.mutable.MapBuilder

import pl.umk.bugclassification.scmparser.training.ModelDAO

class GerritSshProjectPreparationInvoker(private val port: Int, private val hostname: String,
  private val user: String, private val directory: String,
  private val modelDao: ModelDAO) extends ProjectPreparationInvoker {
  def dirUrl = directory

  private def getAllProjects: List[String] = {
    createProcessBuilder(GerritSshLsProjectsCommand(port, hostname)).lines.toList
  }

  private def cloneAndCreateProjectInvokers(projects: List[String]): Map[String, ProjectInvoker] = {
    projects.foreach(projectName =>
      createProcessBuilder(GitCloneProjectFromGerritCommand(port, hostname, user, projectName)).
        run())

    createProjectInvokers(projects)
  }

  private def createProjectInvokers(projects: List[String]): Map[String, ProjectInvoker] = {
    val mapBuilder = new MapBuilder[String, ProjectInvoker, Map[String, ProjectInvoker]](Map.empty)
    projects.foreach(projectName =>
      mapBuilder += projectName -> new GerritSshProjectInvoker(port, hostname, user, projectName, directory + "/" + projectName, modelDao))

    mapBuilder.result
  }

  def cloneAllProjects: Map[String, ProjectInvoker] = {
    val projects = getAllProjects
    cloneAndCreateProjectInvokers(projects)
  }

  def cloneMissingProjects(currentProjects: List[String]): Map[String, ProjectInvoker] = {
    val projects = getAllProjects.filterNot(projectName => currentProjects.contains(projectName))
    createProjectInvokers(currentProjects) ++ cloneAndCreateProjectInvokers(projects)
  }

}