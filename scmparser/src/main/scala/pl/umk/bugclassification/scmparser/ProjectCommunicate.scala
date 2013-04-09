package pl.umk.bugclassification.scmparser

abstract sealed class ProjectCommunicate
case class Learn(branch: String) extends ProjectCommunicate
case class Classify(ref: String, sha1: String) extends ProjectCommunicate