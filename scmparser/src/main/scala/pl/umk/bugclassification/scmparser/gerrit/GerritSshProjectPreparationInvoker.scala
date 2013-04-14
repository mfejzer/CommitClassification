package pl.umk.bugclassification.scmparser.gerrit
import scala.collection.mutable.MapBuilder
import scala.sys.process.ProcessBuilder
import scala.sys.process.ProcessLogger

import pl.umk.bugclassification.scmparser.git.GitLogNoMergesCommand
import pl.umk.bugclassification.scmparser.training.ModelDAO
import pl.umk.bugclassification.scmparser.RmCommand

class GerritSshProjectPreparationInvoker(private val port: Int, private val hostname: String,
  private val user: String, private val directory: String,
  private val modelDao: ModelDAO) extends ProjectPreparationInvoker {
  def dirUrl = directory

  private def getAllProjects: List[String] = {
    createProcessBuilder(GerritSshLsProjectsCommand(port, hostname)).lines.toList
  }

  private def clone(projectName: String): ProcessBuilder = {
    createProcessBuilder(GitCloneProjectFromGerritCommand(port, hostname, user, projectName))
  }

  private def checkIfValid(projectName: String): ProcessBuilder = {
    createProcessBuilder(GitLogNoMergesCommand, "/" + projectName)
  }

  private def rm(projectName: String): ProcessBuilder = {
    createProcessBuilder(RmCommand(projectName))
  }

  private def cloneAndCreateProjectInvokers(projects: List[String]): Map[String, ProjectInvoker] = {
    val validatedProjects = projects.
      map(projectName => (projectName,
        (clone(projectName) #&& checkIfValid(projectName)) ! ProcessLogger(line => ())))

    val correctProjects = validatedProjects.filter(x => x._2 == 0).map(x => x._1)
    val incorrectProjects = validatedProjects.filter(x => x._2 != 0).map(x => x._1)
    incorrectProjects.foreach(x => rm(x).run())

    createProjectInvokers(correctProjects)
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