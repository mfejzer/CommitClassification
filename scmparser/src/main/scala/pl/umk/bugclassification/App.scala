package pl.umk.bugclassification

import org.rogach.scallop._
import pl.umk.bugclassification.scmparser.LoggingConf
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.training.BagOfWords
import pl.umk.bugclassification.scmparser.training.Trainer
import pl.umk.bugclassification.scmparser.training.WekaWrapper
import pl.umk.bugclassification.scmparser.training.ModelDAOImpl

import scala.util.matching.Regex

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val projectName = opt[String]("projectName", required = true)
  val repoPath = opt[String]("repoPath", required = true)
  //  val user = opt[String]("user", required = true)
  //  val port = opt[Int]("port", required = true)
  //  val hostname = opt[String]("hostname", required = true)
  val printEvalResults = opt[Boolean]("printEvalResults")
  val printAttributes = opt[Boolean]("printAttributes")
}

object App extends LoggingConf {

  def main(args: Array[String]) {
    val conf = new Conf(args)
    //    val port = conf.port.apply()
    //    val hostname = conf.hostname.apply()
    //    val user = conf.user.apply()
    val parserInvoker = new GitParserInvoker(conf.projectName.apply, conf.repoPath.apply)
    val trainer = new Trainer(parserInvoker, new ModelDAOImpl)
    val printEval = conf.printEvalResults.get.getOrElse(false)
    val printAttributes = conf.printAttributes.get.getOrElse(false)
    trainer.invokeWeka(printAttributes, printEval)
  }

}




