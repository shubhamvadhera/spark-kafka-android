package spark.svadhera.service;

import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.resource.Put;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.apache.log4j.Logger;

public class SimpleRestService extends ServerResource {

	private static final Logger logger = Logger.getLogger(SimpleRestService.class);

	@Get
	public String getSomething() {

		//String request = getQuery().getValues("request");

		/*if (logger.isDebugEnabled()) {
			logger.debug("Start getSomething");
			logger.debug("data: '" + request + "'");
		}*/
		
		//String result = KafkaC.refreshMessages();
		//result = request;

		/*if (logger.isDebugEnabled()) {
			logger.debug("result: '" + result + "'");
			logger.debug("End getSomething");
		}*/
		return KafkaC.refreshMessages();
	}

	@Post
	public String postSomething(Representation entity) throws IOException {
		String request;
		
		request = entity.getText();
		//System.out.println(request);
		
		KafkaP.sendMessage(request);
		return request;
	}

	@Put
	public String putSomething(Representation entity) {
		
		String request;
		
		Form form = new Form(entity);
		request = form.getValues("request");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Start putSomething");
			logger.debug("data: '" + request + "'");
		}

		String result = null;
		result = "Response from Restlet Webservice" + request;
			

		if (logger.isDebugEnabled()) {
			logger.debug("result: '" + result + "'");
			logger.debug("End putSomething");
		}
		return result;
	}

	@Delete
	public void deleteSomething(Representation entity) {
		
		String request;
		
		Form form = new Form(entity);
		request = form.getValues("request");

		if (logger.isDebugEnabled()) {
			logger.debug("Start deleteSomething");
			logger.debug("data: '" + request + "'");
		}


		if (logger.isDebugEnabled()) {
			logger.debug("End deleteSomething");
		}
	}
}
