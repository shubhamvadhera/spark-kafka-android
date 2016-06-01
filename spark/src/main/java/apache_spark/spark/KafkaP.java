package apache_spark.spark;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
 
import java.util.HashMap;
import java.util.Map;

public class KafkaP {
	static Map<String, Object> config;
	static KafkaProducer<String, String> producer;
	
	public static void kafkaInit() {
		config = new HashMap<String, Object>();
	    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    producer = new KafkaProducer<String, String>(config);
	}
	
	public static void sendMessageReply(String msg) {
	    
		 
	    //int maxMessages = 5;
	 
	    //int count = 0;
	    //while(count < maxMessages) {
	      producer.send(new ProducerRecord<String, String>("testreply", "msg", msg));
	      //System.out.println("Message send.."+msg);
	    //}
	    //producer.close();
	  }
}
