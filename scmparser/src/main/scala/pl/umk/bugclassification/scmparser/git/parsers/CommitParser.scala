package pl.umk.bugclassification.scmparser.git.parsers
import scala.util.parsing.combinator.RegexParsers
import pl.umk.bugclassification.scmparser.git.parsers.results.Commit

object CommitParser extends RegexParsers with CommonParser {
  override def skipWhitespace = false

  def commitList: Parser[List[Commit]] = (commit).* //<~ (not(sha1)|not(newline))
  def commit: Parser[Commit] = (properCommit<~newline.?) 
  def properCommit = (sha1 ~ author ~ date ~ message ~ filenames) ^^ {
    case s ~ a ~ d ~ m ~ f => {
      new Commit(s, a, d, m, f)
    }
  }
  def noFilesCommit = (sha1 ~ author ~ date ~ message) ^^ {
    case s ~ a ~ d ~ m => {
      new Commit(s, a, d, m, List())
    }
  }
  def noFilesEmptyRepoCommit = (sha1 ~ author ~ date ~ messageWhenNoFilesArePresent) ^^ {
    case s ~ a ~ d ~ m => {
      new Commit(s, a, d, m, List())
    }
  }

  def sha1: Parser[String] =
    ("commit(\\s*)".r ~> sha1Text <~ newline) ^^ { case s => s }

  def author: Parser[String] =
    ("Author:(\\s*)".r ~> authorName <~ newline) ^^ { case an => an }

  def date: Parser[String] =
    ("Date:(\\s*)".r ~> dateValue <~ newline) ^^ { case dv => dv }

  def message: Parser[String] =
    (newline ~> messageText <~ newline) ^^ { case mt => mt }

  def messageWhenNoFilesArePresent: Parser[String] =
    (newline ~> messageText) ^^ { case mt => mt }

  def filenames: Parser[List[String]] =
    (newline ~> (rep1sep(filename, newline)) <~ newline).?  ^^ { case of =>of.getOrElse(List[String]()) }

  def authorName: Parser[String] = ".*[^\\n]".r ^^ { case s => s }

  def dateValue: Parser[String] = "[^\\n]*".r ^^ { case s => s }

  def messageText: Parser[String] = "(\\s{4}.*)*".r ^^ { case s => s }

  def commitsFromLog(input: String): List[Commit] = parseAll(commitList, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => {
      log.error(failure.toString)
      scala.sys.error(failure.toString)
    }
  }
}

