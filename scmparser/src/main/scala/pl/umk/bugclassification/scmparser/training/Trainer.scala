package pl.umk.bugclassification.scmparser.training
import pl.umk.bugclassification.scmparser.git.GitParserInvoker


class Trainer(val repositoryPath: String) {
  val parser = new GitParserInvoker(repositoryPath)

  def prepareTrainingSet(): (List[String],List[String]) = {
    val loggedCommits = parser.listLoggedCommits()
    val errors = loggedCommits.filter(x => x.containsFix())
      .map(x => { parser.findCausesForFix(x) })
      .flatten.toList.map(x=>x._2)
    val nonerrors = loggedCommits.map(c=>c.sha1).filter(sha1 => errors.contains(sha1))

    val result = (errors,nonerrors)
    result
  }
}