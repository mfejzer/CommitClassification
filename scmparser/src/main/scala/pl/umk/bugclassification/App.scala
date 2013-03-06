package pl.umk.bugclassification
import scala.util.matching.Regex
import pl.umk.bugclassification.scmparser.training.BagOfWords

object App {

  def foo(x: Array[String]) = x.foldLeft("")((a, b) => a + b)

  def main(args: Array[String]) {
    //      val pattern = new Regex("\\([^\\)]*\\)")
    //      val str = "aaa ( aa)aaa(Mikołaj Fejzer 2012-08-02 19:32:23 +0200  1)aaaaaaaaa"

    //      println((pattern findAllIn str).mkString(","))

    val combo = "aaa aaa banana banana ychy ychy ychy"
    val bag = new BagOfWords(combo,false)
    println(bag.generateMap())
  }
}
