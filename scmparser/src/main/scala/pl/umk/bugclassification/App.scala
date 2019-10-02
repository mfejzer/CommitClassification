package pl.umk.bugclassification

import org.rogach.scallop._
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.training.{ModelDAOImpl, Trainer, WekaWrapperBuilder}

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val project = opt[String]("project", required = true)
  val directory = opt[String]("directory", required = true)
  val historyLimit = opt[Int]("history", required = true)
}

object App {

  def main(args: Array[String]) {
    val conf = new Conf(args)
    conf.verify()
    val projectName = conf.project.apply()
    val directory = conf.directory.apply()
    val historyLimit = conf.historyLimit.apply()

    val parserInvoker = new GitParserInvoker(projectName, directory, historyLimit)
    val modelDao = new ModelDAOImpl
    val trainer = new Trainer(parserInvoker, WekaWrapperBuilder.getSvmBuilder, modelDao)
    trainer.measurePerformance

  }

}




