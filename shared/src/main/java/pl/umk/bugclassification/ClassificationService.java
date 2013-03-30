package pl.umk.bugclassification;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://ws.cxf.java2/", name = "ClassificationService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface ClassificationService {

	@WebMethod
	boolean classificateCommit(
			@WebParam(mode = WebParam.Mode.IN, name = "project", targetNamespace = "http://ws.cxf.java2/") String project,
			@WebParam(mode = WebParam.Mode.IN, name = "commitContent", targetNamespace = "http://ws.cxf.java2/") List<String> commitContent);

}
