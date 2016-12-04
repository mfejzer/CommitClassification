package pl.umk.bugclassification.scmparser.training

import org.slf4j.LoggerFactory

class Classificator(private val modelDao: ModelDAO) {
  val log = LoggerFactory.getLogger(classOf[Classificator])

  /**
    * Returns true if commit is buggy for project, false otherwise
    */
  def classificateCommit(project: String, comitContent: List[String]): Boolean = {
    val maybeModel = modelDao.getModel(project)
    if (maybeModel.isDefined) {
      val classifier = maybeModel.get._1
      val keys = maybeModel.get._2
      val bag = new BagOfWords(comitContent)
      val wrapper = new WekaSvmWrapper()
      val instances = wrapper.createClassificationInstances(bag, keys.toArray[String])
      val result = classifier.classifyInstance(instances.firstInstance()) //1.0 for clean, otherwise buggy
      log.info("classificateCommit result " + result)
      result != 1.0
    } else {
      false
    }
  }
}