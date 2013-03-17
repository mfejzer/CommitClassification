package pl.umk.bugclassification
import scala.util.matching.Regex
import pl.umk.bugclassification.scmparser.training.BagOfWords
import pl.umk.bugclassification.scmparser.training.WekaWrapper

object App {

  def foo(x: Array[String]) = x.foldLeft("")((a, b) => a + b)

  def main(args: Array[String]) {
    val combo = "aaa aaa banana banana ychy ychy ychy"
    val bag = new BagOfWords(List(combo), false)
    
    val combo2 = "aaa ff banana banana mniam ychy mniam"
    val bag2 = new BagOfWords(List(combo2), false)

    val bags = List(bag,bag2)
//    WekaWrapper.example()
    val wekaWrapper = new WekaWrapper()
    println(wekaWrapper.generateInstances(bags))
  }
}
