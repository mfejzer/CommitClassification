package pl.umk.bugclassification.scmparser.training

import java.util.Random

import org.slf4j.{Logger, LoggerFactory}
import weka.classifiers.Classifier
import weka.classifiers.Evaluation
import weka.core.Instances

class WekaSvmWrapper extends WekaWrapper {
  private val classifier = new weka.classifiers.functions.LibSVM()

  def train(instances: Instances) = {
    classifier.buildClassifier(instances)
  }

  def saveModel(): Classifier = {
    Classifier.makeCopy(classifier)
  }

  def printEvaluation(instances: Instances) = {
    val valuation = new Evaluation(instances)
    valuation.crossValidateModel(classifier, instances, 10, new Random(1))
    log.info(valuation.toSummaryString)
  }

  override val log: Logger = LoggerFactory.getLogger(classOf[WekaSvmWrapper])
}