package pl.umk.bugclassification.scmparser.training
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import weka.core.Attribute

@RunWith(classOf[JUnitRunner])
class WekaWrapperSuite extends FunSuite {

  test("check generateInstances on list containing two instances of BagOfWords") {
    val bag = new ClassifiedBagOfWords(List("aaa aaa banana banana ychy ychy ychy"), false)
    val bag2 = new ClassifiedBagOfWords(List("aaa ff banana banana mniam ychy mniam"), true)
    val bags = List(bag, bag2)
    val keys = Array[String]("aaa", "banana", "ychy", "mniam", "ff")
    val wekaWrapper = new WekaSvmWrapper()
    val instances = wekaWrapper.generateInstances(bags, keys)
    assert(instances.numAttributes() === 6)
    assert(instances.numInstances() === 2)
    //Check if last attribute (Classification) is set to clean (1)
    assert(1.0 === instances.instance(0).value(5))
    //Check if last attribute (Classification) is set to buggy (0)
    assert(0.0 === instances.instance(1).value(5))
  }

  test("check generateAttributes") {
    val keys = Array("aaa", "banana", "ychy", "mniam", "ff")
    val wekaWrapper = new WekaSvmWrapper()
    val result = wekaWrapper.generateAttributes(keys)
    val attributes = result._1
    val buggyValue = result._2
    val cleanValue = result._3
    assert(keys.size + 1 === attributes.size())
    assert(keys.indices.forall(index =>
      keys(index) == attributes.get(index).name()))
    assert("class" === attributes.get(attributes.size()-1).name())
    assert(0.0 === buggyValue)
    assert(1.0 === cleanValue)
  }
}