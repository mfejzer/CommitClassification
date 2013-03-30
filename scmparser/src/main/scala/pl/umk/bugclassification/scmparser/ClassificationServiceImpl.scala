package pl.umk.bugclassification.scmparser

import javax.jws.{ WebMethod, WebParam, WebService }
import javax.jws.soap.SOAPBinding
import javax.jws.soap.SOAPBinding.Style
import javax.xml.ws.Endpoint
import pl.umk.bugclassification.ClassificationService

@WebService(endpointInterface = "pl.umk.bugclassification.ClassificationService",
            serviceName = "ClassificationService")
class ClassificationServiceImpl extends ClassificationService {

  def classificateCommit(project: String, commitContent: java.util.List[String]): Boolean = {
    println("received")
    println(project)
    List(commitContent).foreach(x => println(x))
    println("responded with false")
    false
  }

}

object ClassificationServiceLauncher {
  def start {
    val endpoint = Endpoint.publish("http://localhost:8000/classificationService", new ClassificationServiceImpl())
    println("Waiting for requests...")
  }
}