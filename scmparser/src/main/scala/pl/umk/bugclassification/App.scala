package pl.umk.bugclassification

import org.rogach.scallop._
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.training.BagOfWords
import pl.umk.bugclassification.scmparser.training.Trainer
import pl.umk.bugclassification.scmparser.training.WekaWrapper

import scala.util.matching.Regex

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val repoPath = opt[String]("repoPath", required = true)
  val printEvalResults = opt[Boolean]("printEvalResults")
  val printAttributes = opt[Boolean]("printAttributes")
}

object App {

  def main(args: Array[String]) {
    val conf = new Conf(args)
    val parserInvoker = new GitParserInvoker(conf.repoPath.get.get)
    val trainer = new Trainer(parserInvoker)
    val printEval = conf.printEvalResults.get.getOrElse(false)
    val printAttributes = conf.printAttributes.get.getOrElse(false)
    trainer.invokeWeka(printAttributes, printEval)
  }

  def debug() {
    val combo = "aaa aaa banana banana ychy ychy ychy"
    val bag = new BagOfWords(List(combo), false)

    val combo2 = "aaa ff banana banana mniam ychy mniam"
    val bag2 = new BagOfWords(List(combo2), false)

    val bags = List(bag, bag2)

    val wekaWrapper = new WekaWrapper()
    println(wekaWrapper.generateInstances(bags))
  }
}
