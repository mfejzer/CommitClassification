package pl.umk.bugclassification.scmparser.training

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.classifiers.functions.SMO
import weka.classifiers.Evaluation
import java.util.Random
import weka.classifiers.Classifier
import java.util.Date
import scala.collection.mutable.ArrayBuilder
import scala.collection.mutable.ArrayBuilder.ofDouble
import com.codahale.logula.Logging

class WekaWrapper extends Logging {
  private val classifier = new weka.classifiers.functions.LibSVM()

  private def generateAttributes(keys: Array[String]): (FastVector, Double, Double) = {
    val atts = new FastVector()
    keys.map(x => atts.addElement(new Attribute(x)))

    val classificationAttributeValues = new FastVector();
    classificationAttributeValues.addElement("buggy")
    classificationAttributeValues.addElement("clean")
    val classificationAttribute = new Attribute("WekaWrapperClassification", classificationAttributeValues)
    atts.addElement(classificationAttribute);

    (atts, classificationAttributeValues.indexOf("buggy").toDouble, classificationAttributeValues.indexOf("clean").toDouble)
  }

  def generateInstancesAndKeys(bags: List[ClassifiedBagOfWords]): (Instances, Array[String]) = {
    log.info("generateInstancesAndKeys bags size " + bags.size)
    val keys = bags.map(bag => bag.map.keySet.toList).flatten.removeDuplicates.toArray
    log.info("generateInstancesAndKeys keys size " + keys.size)
    val attributes = generateAttributes(keys)
    val atts = attributes._1
    val buggyValue = attributes._2
    val cleanValue = attributes._3
    log.info("generateInstancesAndKeys before createTrainingInstance for each instance ")
    val instances = new Instances("Training", atts, 0);
    bags.par.
      map(bag => createTrainingInstance(bag, keys, buggyValue, cleanValue)).
      foreach(instance => instances.add(instance))
    instances.setClassIndex(instances.numAttributes() - 1)
    log.info("generateInstancesAndKeys after createTrainingInstance for each instance ")
    (instances, keys)
  }

  private def populateValues(bag: BagOfWords, keys: Array[String]): ofDouble = {
    val bagParMap = bag.map.par
    val b = new ofDouble()
    b.sizeHint(keys.size)
    var i = 0
    while (i < keys.size) {
      b += bagParMap.get(keys(i)).orElse(Some(0)).get.toDouble
      i += 1
    }
    b
  }

  def createTrainingInstance(bag: ClassifiedBagOfWords, keys: Array[String], buggyValue: Double, cleanValue: Double): Instance = {
    val b = populateValues(bag, keys)
    if (bag.isBug) {
      b += buggyValue
    } else {
      b += cleanValue
    }
    val values = b.result
    val instance = new Instance(1.0, values)

    instance
  }

  def createClassificationInstances(bag: BagOfWords, keys: Array[String]): Instances = {
    val attributes = generateAttributes(keys)
    val values = (populateValues(bag, keys) += 0).result
    val instance = new Instance(1.0, values)
    instance
    val instances = new Instances("Classification", attributes._1, 0)
    instances.setClassIndex(instances.numAttributes() - 1)
    instances.add(instance)
    instances
  }

  def trainSvm(instances: Instances) = {
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

}