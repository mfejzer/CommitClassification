package pl.umk.bugclassification.scmparser.training
import weka.classifiers.Classifier

trait ModelDAO {
  def saveModel(project: String, model: Classifier, keys:List[String])
  def getModel(project: String): Option[(Classifier,List[String])]
}