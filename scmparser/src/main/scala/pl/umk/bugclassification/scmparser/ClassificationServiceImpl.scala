package pl.umk.bugclassification.scmparser

import javax.jws.{ WebMethod, WebParam, WebService }
import javax.jws.soap.SOAPBinding
import javax.jws.soap.SOAPBinding.Style
import javax.xml.ws.Endpoint
import pl.umk.bugclassification.ClassificationService
import pl.umk.bugclassification.scmparser.training.ModelDAO
import pl.umk.bugclassification.scmparser.training.Classificator
import scala.collection.JavaConverters._

@WebService(endpointInterface = "pl.umk.bugclassification.ClassificationService",
  serviceName = "ClassificationService")
class ClassificationServiceImpl(private val classificator: Classificator) extends ClassificationService {

  def classificateCommit(project: String, commitContent: java.util.List[String]): Boolean = {
    println("received")
    println(project)
    val result = classificator.classificateCommit(project, commitContent.asScala.toList)
    println("responded with " + result)
    result
  }

}

class ClassificationServiceLauncher(private val classificator: Classificator) {
  def start {
    val endpoint = Endpoint.publish("http://localhost:8000/classificationService",
      new ClassificationServiceImpl(classificator))
    println("Waiting for requests...")
  }
}