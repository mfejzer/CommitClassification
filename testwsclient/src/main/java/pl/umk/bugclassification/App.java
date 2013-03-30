package pl.umk.bugclassification;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.setServiceClass(ClassificationService.class);
		// factory.setAddress(endpointAddress);
		factory.setAddress("http://localhost:8000/classificationService");
		ClassificationService service = (ClassificationService) factory
				.create();
		List<String> content = new ArrayList<String>();
		content.add("AAA");
		content.add("EEE");
		System.out.println(service.classificateCommit("kbc", content));

	}
}
