package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.InvokerOnDirectory

trait ProjectPreparationInvoker extends InvokerOnDirectory {

  def cloneAllProjects: Map[String, ProjectInvoker]
  def cloneMissingProjects(currentProjects: List[String]): Map[String, ProjectInvoker]
}