package pl.umk.bugclassification.scmparser.training
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import pl.umk.bugclassification.scmparser.TestLoggingConf

@RunWith(classOf[JUnitRunner])
class WekaWrapperSuite extends FunSuite with TestLoggingConf {

  test("check generateInstances on list containing two instances of BagOfWords") {
    val bag = new ClassifiedBagOfWords(List("aaa aaa banana banana ychy ychy ychy"), false)
    val bag2 = new ClassifiedBagOfWords(List("aaa ff banana banana mniam ychy mniam"), true)
    val bags = List(bag, bag2)
    val wekaWrapper = new WekaWrapper()
    val instances = wekaWrapper.generateInstancesAndKeys(bags)._1
    assert(instances.numAttributes() === 6)
    assert(instances.numInstances() === 2)
    //Check if last attribute (Classification) is set to clean (1)
    assert(instances.instance(0).value(5) === 1)
    //Check if last attribute (Classification) is set to buggy (0)
    assert(instances.instance(1).value(5) === 0)
  }
}