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
        log.debug("parsed {Commit={}}", c)
        c
      }
    }

  def sha1: Parser[String] =
    ("commit(\\s*)".r ~> sha1Text <~ newline) ^^ (s => s)

  def author: Parser[String] =
    ("Author:(\\s*)".r ~> authorName <~ newline) ^^ (a => a)

  def date: Parser[String] =
    ("Date:(\\s*)".r ~> dateValue <~ newline) ^^ (dv => dv)

  def message: Parser[String] =
    (newline ~> messageText <~ newline.?) ^^ (mt => mt)

  def filenames: Parser[List[String]] =
    (newline.? ~> rep(filename) <~ newline.?) ^^ { case files => files}

  def filename: Parser[String] = "^(?!.*(commit))\\S+[\\S ]*\\n".r ^^ { case s => s.trim() }

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

