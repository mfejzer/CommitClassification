package pl.umk.bugclassification.scmparser

package object messages {
  abstract sealed class Message
  case class PreprareAllProjects() extends Message
  case class PreprareMissingProjects() extends Message
  case class LearnOnAllProjects() extends Message
  case class LearnOnProject(project: String) extends Message
  case class LearnOnProjectAndBranch(project: String, branch: String) extends Message
  case class ClassifyOnProject(project: String, ref: String, sha1: String) extends Message

  abstract sealed class ProjectMessage
  case class Learn(branch: String) extends ProjectMessage
  case class Classify(ref: String, sha1: String) extends ProjectMessage
}