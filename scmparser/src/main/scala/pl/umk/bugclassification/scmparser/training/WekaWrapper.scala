package pl.umk.bugclassification.scmparser.training

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.classifiers.functions.SMO
import weka.classifiers.Evaluation
import java.util.Random
import weka.classifiers.Classifier

class WekaWrapper {
  private val classifier = new weka.classifiers.functions.LibSVM()

  def generateInstancesAndKeys(bags: List[ClassifiedBagOfWords]): (Instances, List[String]) = {
    val keys = bags.map(bag => bag.map.keySet.toList).flatten.removeDuplicates

    val atts = new FastVector()
    keys.map(x => atts.addElement(new Attribute(x)))

    val classificationAttributeValues = new FastVector();
    classificationAttributeValues.addElement("buggy")
    classificationAttributeValues.addElement("clean")
    val classificationAttribute = new Attribute("Classification", classificationAttributeValues)
    atts.addElement(classificationAttribute);

    val instances = new Instances("MyRelation", atts, 0);
    bags.
      map(bag => createTrainingInstance(bag, keys, classificationAttributeValues)).
      foreach(instance => instances.add(instance))
    instances.setClassIndex(instances.numAttributes() - 1)

    (instances, keys)
  }

  private def populateValues(bag: BagOfWords, keys: List[String]): Array[Double] = {
    val values = new Array[Double](keys.size + 1)
    for (i <- 0 to keys.size - 1) {
      values(i) = bag.map.getOrElse(keys(i), 0).toDouble
    }
    values
  }

  def createTrainingInstance(bag: ClassifiedBagOfWords, keys: List[String], classificationAttributeValues: FastVector): Instance = {
    //    val values = new Array[Double](keys.size + 1)
    //    for (i <- 0 to keys.size - 1) {
    //      values(i) = bag.map.getOrElse(keys(i), 0).toDouble
    //    }
    val values = populateValues(bag, keys)
    if (bag.isBug) {
      values(keys.size) = classificationAttributeValues.indexOf("buggy")
    } else {
      values(keys.size) = classificationAttributeValues.indexOf("clean")
    }
    val instance = new Instance(1.0, values)

    instance
  }

  def createClassificationInstance(bag: BagOfWords, keys: List[String]) = {
    val values = populateValues(bag, keys)
    val instance = new Instance(1.0, values)
    instance.setClassMissing()
    instance
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
    println(valuation.toSummaryString)
  }

}