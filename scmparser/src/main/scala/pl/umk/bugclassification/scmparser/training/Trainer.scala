package pl.umk.bugclassification.scmparser.training
import pl.umk.bugclassification.scmparser.git.GitParserInvoker
import pl.umk.bugclassification.scmparser.git.ParserInvoker

class Trainer(private val parserInvoker: ParserInvoker) {

  def prepareSha1WithClassificationForTrainingSet(): (List[(String, Boolean)]) = {
    val loggedCommits = parserInvoker.listLoggedCommits()
    val errorsSHA1s = loggedCommits
      .filter(commit => commit.containsFix())
      .map(fixingCommit => { parserInvoker.findCausesForFix(fixingCommit) })
      .flatten.toList.map(x => x._2)

    val result = loggedCommits
      .map(commit => commit.sha1)
      .map(sha1 => (sha1, errorsSHA1s.contains(sha1)))

    result
  }

  def prepareTrainingSet(): List[BagOfWords] = {
    val sha1s = prepareSha1WithClassificationForTrainingSet()
    val result = sha1s.
      map(x => new BagOfWords(parserInvoker.showCommit(x._1), x._2))

    result
  }

  def invokeWeka() {
    val wekaWrapper = new WekaWrapper()
    val instances = wekaWrapper.generateInstances(prepareTrainingSet())
  }
}