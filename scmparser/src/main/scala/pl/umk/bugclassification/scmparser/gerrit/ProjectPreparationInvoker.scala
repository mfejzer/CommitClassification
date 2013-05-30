package pl.umk.bugclassification.scmparser.gerrit

import pl.umk.bugclassification.scmparser.invokers.InvokerOnDirectory

trait ProjectPreparationInvoker extends InvokerOnDirectory {

  def cloneAllProjects: Map[String, ProjectInvoker]
  def cloneMissingProjects(currentProjects: List[String]): Map[String, ProjectInvoker]
  def prepareExistingProjects(currentProjects: List[String]): Map[String, ProjectInvoker]
}