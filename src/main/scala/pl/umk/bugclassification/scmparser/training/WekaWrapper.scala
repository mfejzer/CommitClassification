package pl.umk.bugclassification.scmparser.training

import java.util
import java.util.ArrayList

import org.slf4j.LoggerFactory
import weka.classifiers.Classifier
import weka.core.{Attribute, DenseInstance, FastVector, Instance, Instances}

import scala.collection.mutable.ArrayBuilder.ofDouble

trait WekaWrapper {

  val log = LoggerFactory.getLogger(classOf[WekaWrapper])

  def generateAttributes(keys: Array[String]): (ArrayList[Attribute], Double, Double) = {
    log.info("generateAttributes before creation of attributes")
    val attributes: util.ArrayList[Attribute] = new util.ArrayList[Attribute]

    keys.foreach(key => attributes.add(new Attribute(key)))

    val classificationAttributeValues = new util.ArrayList[String]
    classificationAttributeValues.add("buggy")
    classificationAttributeValues.add("clean")

    val classificationAttribute = new Attribute("WekaWrapperClassification", classificationAttributeValues)
    attributes.add(classificationAttribute)
    log.info("generateAttributes after creation of attributes")

    (attributes, classificationAttributeValues.indexOf("buggy").toDouble, classificationAttributeValues.indexOf("clean").toDouble)
  }

  def generateInstances(bags: List[ClassifiedBagOfWords], keys: Array[String]): Instances = {
    val (attributes, buggyValue, cleanValue) = generateAttributes(keys)

    log.info("generateInstances before createTrainingInstance for each instance ")
    val instances = new Instances("Training", attributes, 0)
    bags.par.
      map(bag => createTrainingInstance(bag, keys, buggyValue, cleanValue)).seq.
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
    val instance = new DenseInstance(1.0, values)

    instance
  }

  def createClassificationInstances(bag: BagOfWords, keys: Array[String]): Instances = {
    val attributes = generateAttributes(keys)
    val values = (populateValues(bag, keys) += 0).result
    val instance = new DenseInstance(1.0, values)

    val instances = new Instances("Classification", attributes._1, 0)
    instances.setClassIndex(instances.numAttributes() - 1)
    instances.add(instance)
    instances
  }

  def train(instances: Instances)

  def saveModel: Classifier

  def printEvaluation(instances: Instances)

}
