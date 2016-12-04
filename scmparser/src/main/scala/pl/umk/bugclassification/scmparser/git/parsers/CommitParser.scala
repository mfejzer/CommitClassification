package pl.umk.bugclassification.scmparser.git.parsers
import org.slf4j.LoggerFactory
import pl.umk.bugclassification.scmparser.git.parsers.results.Commit

import scala.util.parsing.combinator.RegexParsers

object CommitParser extends RegexParsers with CommonParser {

  val log = LoggerFactory.getLogger(CommitParser.getClass)

  override def skipWhitespace = false

  def commitList: Parser[List[Commit]] = commit.* //<~ (not(sha1)|not(newline))
  def commit: Parser[Commit] = properCommit <~ newline.?
  def properCommit =
    (sha1 ~ author ~ date ~ message ~ filenames) ^^ {
      case s ~ a ~ d ~ m ~ f => {
        val c = new Commit(s, a, d, m, f)
        log.debug("CommitParser properCommit parsed " + c)
        c
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
    (newline ~> (rep1sep(filename, newline)) <~ newline).? ^^ { case of => of.getOrElse(List[String]()) }

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

