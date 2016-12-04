package pl.umk.bugclassification.scmparser.git.parsers

import scala.util.parsing.combinator.RegexParsers


trait CommonParser extends RegexParsers {
  def sha1Text: Parser[String] = "[0-9a-f]{40}".r ^^ { case s => s }

  def filename: Parser[String] = "\\S+".r ^^ { case s => s }

  def newline = "\\n".r

}