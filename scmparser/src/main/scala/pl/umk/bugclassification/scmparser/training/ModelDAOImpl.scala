package pl.umk.bugclassification.scmparser.training
import weka.classifiers.Classifier
import java.io.PrintWriter
import java.io.File
import scala.io.Source

class ModelDAOImpl extends ModelDAO {
  def saveModel(project: String, model: Classifier, keys: List[String]) = {
    weka.core.SerializationHelper.write(project + ".model", model)
    val writer = new PrintWriter(new File(project + ".keys"))
    keys.foreach { key => writer.println(key) }
    writer.close()
  }
  def getModel(project: String): Option[(Classifier, List[String])] = {
    if (new File(project + ".model").exists && new File(project + ".keys").exists) {
      val model = weka.core.SerializationHelper.read(project + ".model").asInstanceOf[Classifier]
      val keys = Source.fromFile(project + ".keys").getLines().toList
      Option((model, keys))
    } else Option.empty
  }
}