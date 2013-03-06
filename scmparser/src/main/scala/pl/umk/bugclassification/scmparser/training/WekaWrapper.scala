package pl.umk.bugclassification.scmparser.training

import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances

object WekaWrapper {
  def generateInstances(bags: List[BagOfWords]): Instances = {
    val atts = new FastVector()
    atts.addElement(new Attribute("att1"));
    // - nominal
    val attVals = new FastVector();
    //     for (i = 0; i < 5; i++)
    //       attVals.addElement("val" + (i+1));
    atts.addElement(new Attribute("att2", attVals));
    // - string
    atts.addElement(new Attribute("att3", null: (FastVector)));
    // - date
    atts.addElement(new Attribute("att4", "yyyy-MM-dd"));
    // - relational
    val attsRel = new FastVector();
    // -- numeric
    attsRel.addElement(new Attribute("att5.1"));
    // -- nominal
    val attValsRel = new FastVector();
    //     for (i = 0; i < 5; i++)
    //       attValsRel.addElement("val5." + (i+1));
    attsRel.addElement(new Attribute("att5.2", attValsRel));
    val dataRel = new Instances("att5", attsRel, 0);
    atts.addElement(new Attribute("att5", dataRel, 0));

    // 2. create Instances object
    val data = new Instances("MyRelation", atts, 0);
    return data
  }
}