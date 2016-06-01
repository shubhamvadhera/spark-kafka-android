package spark.svadhera.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaC {
	
	static Map<String, Object> config;
	static KafkaConsumer<String, String> consumer;
	static ConsumerRecords<String, String> records;
	
	public static void kafkaCInit() {
		config = new HashMap<String, Object>();
	    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    config.put("group.id", "default");
	    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	    consumer = new KafkaConsumer<String, String>(config);
	    consumer.subscribe(Arrays.asList("testreply"));
	}
	
	public static String refreshMessages() {
		
         records = consumer.poll(100);
         //System.out.println("COUNT" + records.count());
	         for (ConsumerRecord<String, String> record : records)
	             //System.out.printf("KAFKACONSUMER: " + record.value());
	        	 return record.value();
			return null;
	}
}
