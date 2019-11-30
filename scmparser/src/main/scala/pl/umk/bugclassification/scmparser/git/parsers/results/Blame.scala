package pl.umk.bugclassification.scmparser.git.parsers.results

class Blame(val sha1: String, val metadata: String, val line: String) {

  override def toString = {
    "Blame\n" +
      "Sha1: " + sha1 + "\n" +
      "Metadata: " + metadata + "\n" +
      "Line:" + line + "\n"
  }
}