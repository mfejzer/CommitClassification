package pl.umk.bugclassification.scmparser.gerrit
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.InvokerOnDirectory

class GerritSshProjectsInvoker(private val port: Int, private val hostname: String, private val directory: String) extends InvokerOnDirectory {
  def dirUrl = directory

  private def getAllProjects: List[String] = {
    createProcessBuilder(GerritSshLsProjects(port, hostname)).lines.toList
  }

  private def cloneAndCreateParserInvoker(projects: List[String]): List[GitParserInvoker] = {
    projects.foreach(projectName =>
      createProcessBuilder(GitCloneProjectFromGerrit(port, hostname, projectName)).
        run())

    createParserInvokers(projects)
  }

  private def createParserInvokers(projects: List[String]) = {
    projects.map(projectName => new GitParserInvoker(projectName, directory + "/" + projectName))
  }

  def cloneAllProjects: List[GitParserInvoker] = {
    val projects = getAllProjects
    cloneAndCreateParserInvoker(projects)
  }

  def cloneMissingProjects(currentProjects: List[String]): List[GitParserInvoker] = {
    val projects = getAllProjects.filterNot(projectName => currentProjects.contains(projectName))
    createParserInvokers(currentProjects) ++ cloneAndCreateParserInvoker(projects)
  }

}