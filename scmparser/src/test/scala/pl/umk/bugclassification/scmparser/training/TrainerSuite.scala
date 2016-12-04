package pl.umk.bugclassification.scmparser.training

import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import pl.umk.bugclassification.scmparser.git.ParserInvoker

@RunWith(classOf[JUnitRunner])
class TrainerSuite extends FunSuite with MockFactory {
  test("check key generation") {
    val bag = new ClassifiedBagOfWords(List("aaa aaa banana banana ychy ychy ychy"), false)
    val bag2 = new ClassifiedBagOfWords(List("aaa ff banana banana mniam ychy mniam"), true)
    val bags = List(bag, bag2)
    val correctKeys = Array("aaa", "banana", "ychy", "mniam", "ff")

    val parserInvoker = mock[ParserInvoker]
    val wekaWrapper = mock[WekaWrapper]
    val modelDao = mock[ModelDAO]
    val trainer = new Trainer(parserInvoker, wekaWrapper, modelDao)
    val keys = trainer.prepareKeysForTrainingSet(bags)

    assert(correctKeys.size === keys.size)
    assert(correctKeys.forall(key => keys.contains(key)))
  }
}