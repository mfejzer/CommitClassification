package pl.umk.bugclassification.scmparser

trait Command {
  def command: java.util.List[String]
}