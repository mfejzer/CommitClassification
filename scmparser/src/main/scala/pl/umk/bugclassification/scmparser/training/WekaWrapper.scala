package pl.umk.bugclassification.scmparser.training

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances

class WekaWrapper {
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

    val data = new Instances("MyRelation", atts, 0);
    bags.map(bag => createInstance(bag, keys, classificationAttributeValues)).foreach(instance => data.add(instance))
    data
  }

  def createInstance(bag: BagOfWords, keys: List[String], classificationAttributeValues: FastVector): Instance = {
    val values = new Array[Double](keys.size + 1)
    for (i <- 0 to keys.size-1) {
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

}