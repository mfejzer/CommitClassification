package pl.umk.bugclassification.scmparser.training

class BagOfWords(showOfCommit: String, isBug: Boolean) {
  def generateMap(): Map[String, Integer] = {
    def combine(map: Map[String, Integer], added: String): Map[String, Integer] = added match {
      case "" => map
      case "\n" => map
      case _ => map + ((added, (map.getOrElse(added, 0): Integer) + 1): (String, Integer))
    }
    val splited = showOfCommit.split(" ");
    val map = splited.foldLeft(Map[String, Integer]())(combine)

    return map
  }
}