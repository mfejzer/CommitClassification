package pl.umk.bugclassification.scmparser

import java.io.File

import scala.Array.canBuildFrom

import pl.umk.bugclassification.scmparser.gerrit.GerritSshProjectPreparationInvoker
import pl.umk.bugclassification.scmparser.training.ModelDAO

class ProjectPreparer(private val port: Int, private val hostname: String,
                      private val user: String, private val directory: String,
                      private val historyLimit: Int, private val modelDao: ModelDAO) {

  private val projectPreparationInvoker = new GerritSshProjectPreparationInvoker(port, hostname,
    user, directory, historyLimit, modelDao)
  private val currentProjects = getProjectsFromDirectory

  def allProjectInvokers = if (currentProjects.isEmpty) {
    projectPreparationInvoker.cloneAllProjects
  } else {
    projectPreparationInvoker.cloneMissingProjects(currentProjects) ++
      projectPreparationInvoker.prepareExistingProjects(currentProjects)
  }

  def missingProjectInvokers = if (currentProjects.isEmpty) {
    projectPreparationInvoker.cloneAllProjects
  } else {
    projectPreparationInvoker.cloneMissingProjects(currentProjects)
  }

  private def getProjectsFromDirectory: List[String] = {
    val listedFiles = new File(directory).listFiles()
    listedFiles.map(file => file.getName()).toList
  }
}