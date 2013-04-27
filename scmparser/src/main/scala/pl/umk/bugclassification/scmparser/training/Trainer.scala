package pl.umk.bugclassification.scmparser.training
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.git.ParserInvoker
import java.util.Date
import com.codahale.logula.Logging

class Trainer(private val parserInvoker: ParserInvoker, private val modelDao: ModelDAO) extends Logging{
  private val wekaWrapper = new WekaWrapper()

  def prepareSha1WithClassificationForTrainingSet(): (List[(String, Boolean)]) = {
    val loggedCommits = parserInvoker.listLoggedCommits()
    val errorsSHA1s = loggedCommits
      .filter(commit => commit.containsFix())
      .map(fixingCommit => { parserInvoker.findCausesForFix(fixingCommit) })
      .flatten.toList.map(x => x._2)

    val result = loggedCommits
      .map(commit => commit.sha1)
      .map(sha1 => (sha1, errorsSHA1s.contains(sha1)))

    result
  }

  def prepareTrainingSet(): List[ClassifiedBagOfWords] = {
    val sha1s = prepareSha1WithClassificationForTrainingSet()
    val result = sha1s.
      map(x => new ClassifiedBagOfWords(parserInvoker.showCommit(x._1), x._2))

    result
  }

  def invokeWeka(printAttributes: Boolean, printEvaluation: Boolean) = {
    log.info("invokeWeka preparing instances")
    val instancesAndKeys = wekaWrapper.generateInstancesAndKeys(prepareTrainingSet())
    val instances = instancesAndKeys._1
    val keys = instancesAndKeys._2
    log.info("invokeWeka before training on instances")
    wekaWrapper.trainSvm(instances)
    modelDao.saveModel(parserInvoker.getProjectName, wekaWrapper.saveModel, keys.toList)
    log.info("invokeWeka after training on instances")
    if (printAttributes) {
      for (i <- 0 to instances.numAttributes() - 1) {
        log.info(instances.attribute(i).toString())
        log.info(instances.attributeStats(i).toString())
      }
    }
    if (printEvaluation) {
      wekaWrapper.printEvaluation(instances)
    }
  }
}
