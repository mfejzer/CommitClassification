package pl.umk.bugclassification.scmparser.git.parsers
import scala.util.parsing.combinator.RegexParsers
import pl.umk.bugclassification.scmparser.git.parsers.results.Commit


object CommitParser extends RegexParsers with CommonParser {
  override def skipWhitespace = false

  def commitList: Parser[List[Commit]] = (commit).* //<~ (not(sha1)|not(newline))
  def commit: Parser[Commit] =
    (sha1 ~ author ~ date ~ message ~ filenames) ^^ {
      case s ~ a ~ d ~ m ~ f => {
        val c = new Commit(s, a, d, m, f)
//        println(c)
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

  def filenames: Parser[List[String]] =
    (newline ~> repsep(filename,newline|"\\Z".r)<~((newline<~newline)|".*\\Z".r)) ^^ { case f => f }

//  def newline = "\\n".r

//  def sha1Text: Parser[String] = "[0-9a-f]{40}".r ^^ { case s => s }
  def authorName: Parser[String] = ".*[^\\n]".r ^^ { case s => s }
  def dateValue: Parser[String] = "[^\\n]*".r ^^ { case s => s }
  def messageText: Parser[String] = "(\\s{4}.*)*".r ^^ { case s => s }
//  def filename: Parser[String] =  "\\S+".r ^^ { case s => s }

  def commitsFromLog(input: String): List[Commit] = parseAll(commitList, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => {
      scala.sys.error(failure.msg)
    }
  }
}

