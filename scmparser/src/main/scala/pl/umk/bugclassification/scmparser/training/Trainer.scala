package pl.umk.bugclassification.scmparser.training
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.git.ParserInvoker
import java.util.Date

class Trainer(private val parserInvoker: ParserInvoker, private val modelDao: ModelDAO) {
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
    println("Trainer preparing instances " + new Date())
    val instancesAndKeys = wekaWrapper.generateInstancesAndKeys(prepareTrainingSet())
    val instances = instancesAndKeys._1
    val keys = instancesAndKeys._2
    println("Trainer before training on instances " + new Date())
    wekaWrapper.trainSvm(instances)
    modelDao.saveModel(parserInvoker.getProjectName, wekaWrapper.saveModel, keys)
    println("Trainer after training on instances " + new Date())
    if (printAttributes) {
      for (i <- 0 to instances.numAttributes() - 1) {
        println(instances.attribute(i))
        println(instances.attributeStats(i))
      }
    }
    if (printEvaluation) {
      wekaWrapper.printEvaluation(instances)
    }
  }
}
