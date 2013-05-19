package pl.umk.bugclassification.scmparser.git.parsers.results
import pl.umk.bugclassification.scmparser.git.BugFixDetectionList

class Commit(val sha1: String, val author: String, val date: String, val message: String, val filenames: List[String]) {
  def containsFix(): Boolean = {
    val result = BugFixDetectionList.detectionList()
      .map(x => { message.contains(x) })
      .reduce((x, y) => (x || y))
    result
  }

  def parent: String = sha1 + "^"

  override def toString = {
    "Sha1 " + sha1 + "\n" +
      "Author " + author + "\n" +
      "Date " + date + "\n" +
      "Message\n" + message + "\n" +
      "Files:" + filenames.mkString("\n") + "\n"
  }

}