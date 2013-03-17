package pl.umk.bugclassification.scmparser.training

class BagOfWords(val showOfCommit: List[String],val isBug: Boolean) {
  def generateMap(): Map[String, Int] = {
    //    def combine(map: Map[String, Integer], added: String): Map[String, Integer] = added match {
    //      case "" => map
    //      case "\n" => map
    //      case _ => map + ((added, (map.getOrElse(added, 0): Integer) + 1): (String, Integer))
    //    }
    //    val splited = showOfCommit.split(" ");
    //    val map = splited.foldLeft(Map[String, Integer]())(combine)
    val map = showOfCommit.map(x => x.split(" ")).flatten.groupBy(x => x).mapValues(x => x.length)
    return (map - "")
  }
  val map=generateMap()
}