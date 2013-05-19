package pl.umk.bugclassification.scmparser.git.parsers
import scala.util.parsing.combinator.RegexParsers
import pl.umk.bugclassification.scmparser.git.parsers.results.Blame

object BlameParser extends RegexParsers with CommonParser {
  override def skipWhitespace = false
  def blameList: Parser[List[Blame]] = blame*
  def blame: Parser[Blame] =
    ((sha1Text|sha1ParentText) ~ ("\\s".r ~> filename) ~ ("\\s+".r ~> metadata) ~ ("\\s".r ~> line)) <~ newline ^^ {
      case s ~ f ~ m ~ l => new Blame(s, f, m, l)
    }

  def sha1ParentText:Parser[String] = ("\\^[0-9a-f]{39}".r) ^^ {case s => s}
  def metadata: Parser[String] = "\\([^\\)]*\\)".r ^^ { case s => s }
  def line: Parser[String] = ".*".r ^^ { case s => s }

  def blamesFromInput(input: String): List[Blame] = parseAll(blameList, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => {
      log.error(failure.toString)
      scala.sys.error(failure.toString)
    }
  }
  
}