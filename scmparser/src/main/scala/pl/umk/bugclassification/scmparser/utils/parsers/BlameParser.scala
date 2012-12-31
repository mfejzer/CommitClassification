package pl.umk.bugclassification.scmparser.utils.parsers
import scala.util.parsing.combinator.RegexParsers
import pl.umk.bugclassification.scmparser.utils.Blame

object BlameParser extends RegexParsers with CommonParser {
  override def skipWhitespace = false
  def blameList: Parser[List[Blame]] = blame*
  def blame: Parser[Blame] =
    (sha1Text ~ ("\\s".r ~> filename) ~ ("\\s+".r ~> metadata) ~ ("\\s".r ~> line)) <~ newline ^^ {
      case s ~ f ~ m ~ l => new Blame(s, f, m, l)
    }

  def metadata: Parser[String] = "\\([^\\)]*\\)".r ^^ { case s => s }
  def line: Parser[String] = ".*".r ^^ { case s => s }

  def blamesFromInput(input: String): List[Blame] = parseAll(blameList, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => {
      scala.sys.error(failure.msg)
    }
  }
}