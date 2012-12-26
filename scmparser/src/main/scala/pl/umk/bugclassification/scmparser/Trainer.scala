package pl.umk.bugclassification.scmparser
import pl.umk.bugclassification.scmparser.utils.GitParserInvoker
import pl.umk.bugclassification.scmparser.utils.Commit

class Trainer(val repositoryPath: String) {
  val parser = new GitParserInvoker(repositoryPath)
  
  def prepareTrainingSet(): List[(Commit, Commit)] = {
    parser.listLoggedCommits()
      .filter(x => x.containsFix())
      .map(x => { parser.findCausesForFix(x) })
      .flatten.toList
  }
}