package pl.umk.bugclassification.scmparser.utils

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

object GitCommand {
  def getGitLogOneline(): java.util.List[String] = {
    return asList(ListBuffer(List("git", "log", "--oneline"): _*))
  }

  def getGitLogNoMerges(): java.util.List[String] = {
    return asList(ListBuffer(List("git", "log"): _*))
  }
  
  def getGitShowCommit(commit: String): java.util.List[String] = {
    return asList(ListBuffer(List("git", "show", commit): _*))
  }

  def getGitShowDiff(commit1: String,commit2:String): java.util.List[String] = {
    return asList(ListBuffer(List("git", "diff", commit1,commit2): _*))
  }
}