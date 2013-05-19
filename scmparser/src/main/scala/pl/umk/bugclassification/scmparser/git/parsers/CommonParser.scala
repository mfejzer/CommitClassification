package pl.umk.bugclassification.scmparser.git.parsers
import com.codahale.logula.Logging
import scala.util.parsing.combinator.RegexParsers


trait CommonParser extends RegexParsers with Logging  {
  def sha1Text: Parser[String] = "[0-9a-f]{40}".r ^^ { case s => s }
  def filename: Parser[String] = "\\S+".r ^^ { case s => s }
  def newline = "\\n".r

}