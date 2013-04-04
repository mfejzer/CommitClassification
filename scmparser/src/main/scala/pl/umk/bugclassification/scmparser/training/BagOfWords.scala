package pl.umk.bugclassification.scmparser.training
import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

class ClassifiedBagOfWords(showOfCommit: List[String], val isBug: Boolean) extends BagOfWords(showOfCommit)

class BagOfWords(val showOfCommit: List[String]) {
  def generateMap(): Map[String, Int] = {
    val map = showOfCommit.
      map(line => line split " ").
      flatten.
      map(word => BagOfWordsWordParser.process(word)).
      flatten.
      groupBy(word => word).
      mapValues(wordList => wordList.length)
    return (map - "")
  }
  val map = generateMap()
}

object BagOfWordsWordParser extends RegexParsers {
  def word: Parser[String] = "[A-Za-z0-9]+".r ^^ { case s => s }
  def operator: Parser[String] = "[^A-Za-z0-9]".r ^^ { case s => s }
  def line = (word | operator)*

  def process(input: String): List[String] = parseAll(BagOfWordsWordParser.line, input) match {
    case Success(result, _) => result
    case failure: NoSuccess => List(input)
  }

}
