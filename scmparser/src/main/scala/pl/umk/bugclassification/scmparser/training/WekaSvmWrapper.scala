package pl.umk.bugclassification.scmparser.training

import java.util.Random

import org.slf4j.{Logger, LoggerFactory}
import weka.classifiers.functions.SMO
import weka.classifiers.{AbstractClassifier, Classifier, Evaluation}
import weka.core.Instances
import weka.filters.Filter
import weka.filters.supervised.instance.ClassBalancer

class WekaSvmWrapper extends WekaWrapper {
  private val classifier = new SMO()

  def train(instances: Instances) = {
    log.info(instances.numInstances().toString)
    val classBalancer = new ClassBalancer
    classBalancer.setInputFormat(instances)
    val trainingInstances = Filter.useFilter(instances, classBalancer)
    log.info(trainingInstances.numInstances().toString)
    classifier.buildClassifier(trainingInstances)
  }

  def saveModel(): Classifier = {
    AbstractClassifier.makeCopy(classifier)
  }

  def printEvaluation(instances: Instances) = {
    val valuation = new Evaluation(instances)
    valuation.crossValidateModel(classifier, instances, 10, new Random(1))
    log.info(valuation.toSummaryString)
    log.info(valuation.toClassDetailsString)
    log.info(valuation.toMatrixString)
    log.info("RESULT;" +
      valuation.precision(0) + ";" +
      valuation.recall(0) + ";" +
      valuation.fMeasure(0) +
      valuation.precision(1) + ";" +
      valuation.recall(1) + ";" +
      valuation.fMeasure(1)
    )
  }

  override val log: Logger = LoggerFactory.getLogger(classOf[WekaSvmWrapper])
}