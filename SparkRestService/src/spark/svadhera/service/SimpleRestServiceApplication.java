package spark.svadhera.service;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class SimpleRestServiceApplication extends Application {
	public SimpleRestServiceApplication() {
		System.out.println("Starting kafka init");
		KafkaP.kafkaInit();
		KafkaC.kafkaCInit();
	}

	/*@Override
	public synchronized Restlet createInboundRoot() {
		
		// Create a router Restlet that routes each call to a
		Router router = new Router(getContext());
		router.attach("/sparkrest", SimpleRestService.class);
		router.attach("/sparkrest/{request}", SimpleRestService.class);
		return router;
	}*/
	@Override
	public synchronized Restlet createInboundRoot() {
		//KafkaProduce.init();
		//otherFunc();
		// Create a router Restlet that routes each call to a
		Router router = new Router(getContext());
		router.attach("/sparkrest", SimpleRestService.class);
		router.attach("/sparkrest/{request}", SimpleRestService.class);
		return router;
	}
	
	public static void otherFunc(){
		System.out.println("Starting kafka init");
		//KafkaProduce.init();
	}
}
