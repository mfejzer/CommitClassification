package pl.umk.bugclassification.scmparser.utils

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

object GitCommand {
  def logOneline(): java.util.List[String] = {
    return asList(ListBuffer(List("git", "log", "--oneline"): _*))
  }

  def logNoMerges(): java.util.List[String] = {
    return asList(ListBuffer(List("git", "log", "--name-only"): _*))
  }

  def showCommit(commit: String): java.util.List[String] = {
    return asList(ListBuffer(List("git", "show", commit): _*))
  }

  def diff(commit1: String, commit2: String): java.util.List[String] = {
    return asList(ListBuffer(List("git", "diff", commit1, commit2): _*))
  }

  def diffOnFileWithParent(commit: Commit, file: String): java.util.List[String] = {
    return asList(ListBuffer(List("git", "diff", commit.sha1, commit.parent, file): _*))
  }

  def blameOnFileWithParent(commit: Commit, file: String): java.util.List[String] = {
    return asList(ListBuffer(List("git", "blame" ,"-l","-f", commit.parent, file ): _*))
  }
}