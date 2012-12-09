package pl.umk.bugclassification.scmparser.utils

class Commit(val sha1: String, val author: String, val date: String, val message: String) {
  def containsFix(): Boolean = {
    val result = BugFixDetectionList.detectionList()
      .map(x => { message.contains(x) })
      .reduce((x, y) => (x && y))
    result
  }

  override def toString = "Sha1 " + sha1 + "\n" + "Author " + author + "\n" + "Date " + date + "\n" + "Message\n" + message + "\n"

}