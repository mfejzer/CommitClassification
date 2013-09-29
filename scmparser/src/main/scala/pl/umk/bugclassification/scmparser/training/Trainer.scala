package pl.umk.bugclassification.scmparser.training
import com.codahale.logula.Logging

import pl.umk.bugclassification.scmparser.git.ParserInvoker
import weka.filters.supervised.instance.StratifiedRemoveFolds
import weka.filters.Filter

class Trainer(private val parserInvoker: ParserInvoker,
  private val wekaWrapper: WekaWrapper,
  private val modelDao: ModelDAO) extends Logging {

  def prepareSha1WithClassificationForTrainingSet(): (List[(String, Boolean)]) = {
    val loggedCommits = parserInvoker.listLoggedCommits()
    log.info("prepareSha1WithClassificationForTrainingSet loggedCommits size " + loggedCommits.size)
    val commitsContainingFixes = loggedCommits
      .filter(commit => commit.containsFix())
    log.info("prepareSha1WithClassificationForTrainingSet commitsContainingFixes size " + commitsContainingFixes.size)
    val errorsSHA1s = commitsContainingFixes
      .map(fixingCommit => { parserInvoker.findCausesForFix(fixingCommit) })
      .flatten.toList.map(x => x._2)
    log.info("prepareSha1WithClassificationForTrainingSet errorsSHA1s size " + errorsSHA1s.size)
    val result = loggedCommits
      .map(commit => commit.sha1)
      .map(sha1 => (sha1, errorsSHA1s.contains(sha1)))

    result
  }

  def prepareTrainingSet(): List[ClassifiedBagOfWords] = {
    val sha1s = prepareSha1WithClassificationForTrainingSet()
    val bags = sha1s.
      map(x => new ClassifiedBagOfWords(parserInvoker.showCommit(x._1), x._2))

    log.info("prepareKeysForTrainingSet bags size " + bags.size)
    bags
  }

  def prepareKeysForTrainingSet(bags: List[ClassifiedBagOfWords]) = {
    val keys = bags.map(bag => bag.map.keySet.toList).flatten.removeDuplicates.toArray
    log.info("prepareKeysForTrainingSet keys size " + keys.size)
    keys
  }

  def invokeWeka(printAttributes: Boolean, printEvaluation: Boolean) = {
    log.info("invokeWeka preparing instances")
    val bags = prepareTrainingSet()
    val keys = prepareKeysForTrainingSet(bags)
    val instances = wekaWrapper.generateInstances(bags, keys)
    log.info("invokeWeka before training on instances")
    wekaWrapper.train(instances)
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

  def measurePerformance = {
    log.info("measurePerformance preparing instances")
    val bags = prepareTrainingSet()
    val keys = prepareKeysForTrainingSet(bags)
    val instances = wekaWrapper.generateInstances(bags, keys)
    log.info("measurePerformance before spliting instances")
    val srfTraining = new StratifiedRemoveFolds()
    srfTraining.setInputFormat(instances)
    srfTraining.setNumFolds(5)
    srfTraining.setFold(5)
    srfTraining.setInvertSelection(true)
    srfTraining.setSeed(1)
    val trainingInstances = Filter.useFilter(instances, srfTraining)
    trainingInstances.setClassIndex(instances.numAttributes() - 1)
    log.info("measurePerformance before training on instances")
    wekaWrapper.train(trainingInstances)
    modelDao.saveModel(parserInvoker.getProjectName, wekaWrapper.saveModel, keys.toList)
    log.info("measurePerformance after training on instances")
    log.info("measurePerformance before testing on instances")
    val srfTesting = new StratifiedRemoveFolds()
    srfTesting.setInputFormat(instances)
    srfTesting.setNumFolds(5)
    srfTesting.setFold(5)
    srfTesting.setInvertSelection(false)
    srfTesting.setSeed(1)
    val testingInstances = Filter.useFilter(instances, srfTesting)
    testingInstances.setClassIndex(instances.numAttributes() - 1)
    wekaWrapper.printEvaluation(testingInstances)
    log.info("measurePerformance after testing on instances")

    log.info(instances.attribute(instances.numAttributes() - 1).toString())
    log.info(instances.attributeStats(instances.numAttributes() - 1).toString())
  }
}
