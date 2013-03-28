package pl.umk.bugclassification.scmparser.training

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.classifiers.functions.SMO
import weka.classifiers.Evaluation
import java.util.Random

class WekaWrapper {
  private val classifier = new SMO()

  def generateInstances(bags: List[BagOfWords]): Instances = {
    val keys = bags.map(x => x.map.keySet.toList).flatten.removeDuplicates
    //val keys = bags.map(x => x.map).reduce((a,b)=>a++b).keySet.toList

    val atts = new FastVector()
    keys.map(x => atts.addElement(new Attribute(x)))

    val classificationAttributeValues = new FastVector();
    classificationAttributeValues.addElement("buggy")
    classificationAttributeValues.addElement("clean")
    val classificationAttribute = new Attribute("Classification", classificationAttributeValues)
    atts.addElement(classificationAttribute);

    val instances = new Instances("MyRelation", atts, 0);
    bags.map(bag => createInstance(bag, keys, classificationAttributeValues)).foreach(instance => instances.add(instance))
    instances.setClassIndex(instances.numAttributes() - 1)

    instances
  }

  def createInstance(bag: BagOfWords, keys: List[String], classificationAttributeValues: FastVector): Instance = {
    val values = new Array[Double](keys.size + 1)
    for (i <- 0 to keys.size - 1) {
      values(i) = bag.map.getOrElse(keys(i), 0).toDouble
    }
    if (bag.isBug) {
      values(keys.size) = classificationAttributeValues.indexOf("buggy")
    } else {
      values(keys.size) = classificationAttributeValues.indexOf("clean")
    }
    val instance = new Instance(1.0, values)

    instance
  }

  def trainSvm(instances: Instances) = {
    classifier.buildClassifier(instances)
  }

  def printEvaluation(instances: Instances) = {
    val valuation = new Evaluation(instances)
    valuation.crossValidateModel(classifier, instances, 10, new Random(1))
    println(valuation.toSummaryString)
  }

}