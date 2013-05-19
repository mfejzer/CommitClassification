package pl.umk.bugclassification.scmparser

abstract sealed class Communicate
case class PreprareAllProjects extends Communicate
case class PreprareMissingProjects extends Communicate
case class LearnOnAllProjects extends Communicate
case class LearnOnProject(project: String) extends Communicate
case class LearnOnProjectAndBranch(project: String, branch: String) extends Communicate
case class ClassifyOnProject(project: String, ref: String, sha1: String) extends Communicate

abstract sealed class ProjectCommunicate
case class Learn(branch: String) extends ProjectCommunicate
case class Classify(ref: String, sha1: String) extends ProjectCommunicate
