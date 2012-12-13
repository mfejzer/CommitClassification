package pl.umk.bugclassification.scmparser.utils
import scala.util.parsing.combinator.RegexParsers

object CommitParser extends RegexParsers {
  override def skipWhitespace = false

  def commitList: Parser[List[Commit]] = commit.*
  def commit: Parser[Commit] =
    (sha1 ~ author ~ date ~ message) ^^ {
      case s ~ a ~ d ~ m => {
        val c = new Commit(s, a, d, m)
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
    (newline ~> messageText <~ (newline*)) ^^ { case mt => mt }

  def newline = "\\n".r

  def sha1Text: Parser[String] = "[0-9a-f]{40}".r ^^ { case s => s }
  def authorName: Parser[String] = ".*[^\\n]".r ^^ { case s => s }
  def dateValue: Parser[String] = "[^\\n]*".r ^^ { case s => s }
  def messageText: Parser[String] = "(\\s{4}.*)*".r ^^ { case s => s } //good
  //  def messageText: Parser[String] = "(?s).*".r ^^ { case s => s }

  def apply(input: String): List[Commit] = parseAll(commitList, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => scala.sys.error(failure.msg)
  }
}

