package pl.umk.bugclassification.scmparser.gerrit.parsers
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonGerritEventStreamParserSuite extends FunSuite {

  test("parsing correct 'patch set created event'") {
    val line = """{"type":"patchset-created","change":{"project":"tmp","branch":"master","id":"Ia3ff6b65602e80209b9509a9ea50241f0791042d","number":"56","subject":"9th","owner":{"name":"Mikołaj Fejzer","email":"mfejzer@gmail.com","username":"mfejzer"},"url":"http://machina:8080/56"},"patchSet":{"number":"2","revision":"0a96b652a4a7f840490b23dab1a6147b04e8865b","parents":["a6402e9705e3bb62d94262f29b002274478acb4b"],"ref":"refs/changes/56/56/2","uploader":{"name":"Mikołaj Fejzer","email":"mfejzer@gmail.com","username":"mfejzer"},"createdOn":1365250060,"author":{"name":"Mikołaj Fejzer","email":"mfejzer@gmail.com","username":"mfejzer"},"sizeInsertions":2,"sizeDeletions":-1},"uploader":{"name":"Mikołaj Fejzer","email":"mfejzer@gmail.com","username":"mfejzer"}}"""

    val event = JsonGerritEventStreamParser.processEvent(line)
    assert(event.isDefined === true)
    assert(event.get.change.project === "tmp")
    assert(event.get.patchSet.ref === "refs/changes/56/56/2")
    assert(event.get.patchSet.revision === "0a96b652a4a7f840490b23dab1a6147b04e8865b")
  }
}