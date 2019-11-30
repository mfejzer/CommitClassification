package pl.umk.bugclassification.scmparser.git.parsers

import org.slf4j.LoggerFactory
import pl.umk.bugclassification.scmparser.git.parsers.results.Blame

import scala.util.parsing.combinator.RegexParsers

object BlameParser extends RegexParsers with CommonParser {

  val log = LoggerFactory.getLogger(BlameParser.getClass)

  override def skipWhitespace = false

  def blameList: Parser[List[Blame]] = blame *

  def blame: Parser[Blame] =
    ((sha1Text | sha1ParentText) ~ ("\\s+".r ~> metadata) ~ ("\\s".r ~> line)) <~ newline ^^ {
      case s ~ m ~ l => new Blame(s, m, l)
    }

  def sha1ParentText: Parser[String] = ("\\^[0-9a-f]{39}".r) ^^ { case s => s }

  def metadata: Parser[String] = "\\([^\\)]*\\)".r ^^ { case s => s }

  def line: Parser[String] = ".*".r ^^ { case s => s }

  def blamesFromInput(input: String): Option[List[Blame]] = parseAll(blameList, input) match {
    case Success(result, _) => Some(result)
    case failure: NoSuccess => {
      log.error(failure.toString)
      None
    }
  }

}