package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.training.ModelDAO
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.training.Trainer

class Learner(private val port: Int, private val hostname: String,
  private val parserInvokers: Map[String, GitParserInvoker],
  private val modelDao: ModelDAO) {
  val trainers = parserInvokers.mapValues(parserInvoker => new Trainer(parserInvoker, modelDao))

  def trainAll = {
    trainers.values.par.foreach(trainer => trainer.invokeWeka(false, false))
  }

  def trainOnProject(project: String) = {
    trainers.get(project).foreach(trainer => trainer.invokeWeka(false, false))
  }
}