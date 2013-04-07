package pl.umk.bugclassification.scmparser.training

class Classificator(private val modelDao: ModelDAO) {
  /**
   * Returns true if commit is buggy for project, false otherwise
   */
  def classificateCommit(project: String, comitContent: List[String]): Boolean = {
    val maybeModel = modelDao.getModel(project)
    if (maybeModel.isDefined) {
      val classifier = maybeModel.get._1
      val keys = maybeModel.get._2
      val bag = new BagOfWords(comitContent)
      val wrapper = new WekaWrapper()
      val instance = wrapper.createClassificationInstance(bag, keys)
      classifier.classifyInstance(instance)
      true//Change to something
    } else {
      false
    }
  }
}