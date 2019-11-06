package pl.umk.bugclassification.scmparser.training

import java.util.Random

import org.slf4j.{Logger, LoggerFactory}
import weka.classifiers.Classifier
import weka.classifiers.Evaluation
import weka.core.Instances
import weka.filters.supervised.instance.SpreadSubsample
import weka.filters.Filter

class WekaSvmWrapper extends WekaWrapper {
  private val classifier = new weka.classifiers.functions.LibSVM()

  def train(instances: Instances) = {
    log.info(instances.numInstances().toString)
    val ff = new SpreadSubsample
    ff.setInputFormat(instances)
    val trainingInstances = Filter.useFilter(instances, ff)
    log.info(trainingInstances.numInstances().toString)
    classifier.buildClassifier(trainingInstances)
  }

  def saveModel(): Classifier = {
    Classifier.makeCopy(classifier)
  }

  def printEvaluation(instances: Instances) = {
    val valuation = new Evaluation(instances)
    valuation.crossValidateModel(classifier, instances, 10, new Random(1))
    log.info(valuation.toSummaryString)
    log.info(valuation.toClassDetailsString)
    log.info(valuation.toMatrixString)
  }

  override val log: Logger = LoggerFactory.getLogger(classOf[WekaSvmWrapper])
}