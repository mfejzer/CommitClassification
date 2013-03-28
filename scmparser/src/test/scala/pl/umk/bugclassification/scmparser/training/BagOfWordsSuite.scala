package pl.umk.bugclassification.scmparser.training
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BagOfWordsSuite extends FunSuite {

  test("check bag of words on 'banana banana ychy ychy ychy'") {
    val bag = new BagOfWords(List("banana banana ychy ychy ychy"), false)
    val map = bag.generateMap()
    assert(!map.isEmpty)
    assert(map.keySet.size === 2)
    assert(map.get("banana").isDefined)
    assert(map.get("banana").get === 2)
    assert(map.get("ychy").isDefined)
    assert(map.get("ychy").get === 3)
  }

  test("check bag of words on two lines") {
    val bag = new BagOfWords(List("banana banana", "ychy ychy ychy"), false)
    val map = bag.generateMap()
    assert(!map.isEmpty)
    assert(map.keySet.size === 2)
    assert(map.get("banana").isDefined)
    assert(map.get("banana").get === 2)
    assert(map.get("ychy").isDefined)
    assert(map.get("ychy").get === 3)
  }

  test("check bag of words on empty string") {
    val bag = new BagOfWords(List(""), false)
    val map = bag.generateMap()
    assert(map.isEmpty)
  }

  test("check bag of words on 'banana'") {
    val bag = new BagOfWords(List("banana"), false)
    val map = bag.generateMap()
    assert(!map.isEmpty)
    assert(map.get("banana").isDefined)
    assert(map.get("banana").get === 1)
  }

  test("check bag of words on '(4+5*banana)'") {
    val bag = new BagOfWords(List("(4+5*(banana)) 5==8"), false)
    val map = bag.generateMap()
    assert(!map.isEmpty)
    println(map)
    assert(map.get("4").isDefined)
    assert(map.get("5").isDefined)
    assert(map.get("banana").isDefined)
    assert(map.get("8").isDefined)
    assert(map.get("+").isDefined)
    assert(map.get("=").isDefined)
    assert(map.get("(").isDefined)
    assert(map.get(")").isDefined)
    assert(map.get("4").get === 1)
    assert(map.get("5").get === 2)
    assert(map.get("banana").get === 1)
    assert(map.get("8").get === 1)
    assert(map.get("+").get === 1)
    assert(map.get("*").get === 1)
    assert(map.get("=").get === 2)
    assert(map.get("(").get === 2)
    assert(map.get(")").get === 2)
  }
}
