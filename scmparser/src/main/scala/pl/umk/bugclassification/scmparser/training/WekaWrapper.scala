package pl.umk.bugclassification.scmparser.training

import scala.Array.canBuildFrom
import scala.collection.mutable.ArrayBuilder.ofDouble

import com.codahale.logula.Logging

import weka.classifiers.Classifier
import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances

trait WekaWrapper extends Logging {

  def generateAttributes(keys: Array[String]): (FastVector, Double, Double) = {
    log.info("generateAttributes begore creation of attributes")
    val atts = new FastVector()
    keys.map(x => atts.addElement(new Attribute(x)))

    val classificationAttributeValues = new FastVector();
    classificationAttributeValues.addElement("buggy")
    classificationAttributeValues.addElement("clean")
    val classificationAttribute = new Attribute("WekaWrapperClassification", classificationAttributeValues)
    atts.addElement(classificationAttribute);
    log.info("generateAttributes after creation of attributes")

    (atts, classificationAttributeValues.indexOf("buggy").toDouble, classificationAttributeValues.indexOf("clean").toDouble)
  }

  def generateInstances(bags: List[ClassifiedBagOfWords], keys: Array[String]): Instances = {
    val attributes = generateAttributes(keys)
    val atts = attributes._1
    val buggyValue = attributes._2
    val cleanValue = attributes._3
    log.info("generateInstances before createTrainingInstance for each instance ")
    val instances = new Instances("Training", atts, 0);
    bags.par.
      map(bag => createTrainingInstance(bag, keys, buggyValue, cleanValue)).
      foreach(instance => instances.add(instance))
    instances.setClassIndex(instances.numAttributes() - 1)
    log.info("generateInstances after createTrainingInstance for each instance ")

    instances
  }

  private def populateValues(bag: BagOfWords, keys: Array[String]): ofDouble = {
    val bagParMap = bag.map.par
    val arrayBuilderOfDouble = new ofDouble()
    arrayBuilderOfDouble.sizeHint(keys.size)
    var i = 0
    while (i < keys.size) {
      arrayBuilderOfDouble += bagParMap.get(keys(i)).orElse(Some(0)).get.toDouble
      i += 1
    }
    arrayBuilderOfDouble
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

  def train(instances: Instances)

  def saveModel: Classifier

  def printEvaluation(instances: Instances)

}