package apache_spark.spark;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;import java.util.regex.Pattern;

import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.examples.streaming.StreamingExamples;

/**
 * Consumes messages from one or more topics in Kafka and does wordcount.
 *
 * Usage: JavaKafkaWordCount <zkQuorum> <group> <topics> <numThreads>
 *   <zkQuorum> is a list of one or more zookeeper servers that make quorum
 *   <group> is the name of kafka consumer group
 *   <topics> is a list of one or more kafka topics to consume from
 *   <numThreads> is the number of threads the kafka consumer should use
 *
 * To run this example:
 *   `$ bin/run-example org.apache.spark.examples.streaming.JavaKafkaWordCount zoo01,zoo02, \
 *    zoo03 my-consumer-group topic1,topic2 1`
 */

public final class JavaWordCount {
  private static final Pattern SPACE = Pattern.compile(" ");

  private JavaWordCount() {
  }

  public static void main(String[] args) throws Exception {
    /*if (args.length < 4) {
      System.err.println("Usage: JavaKafkaWordCount <zkQuorum> <group> <topics> <numThreads>");
      System.exit(1);
    }*/
	  KafkaP.kafkaInit();
    StreamingExamples.setStreamingLogLevels();
    SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local[2]");
    // Create the context with 2 seconds batch size
    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(2000));

    int numThreads = 1;//Integer.parseInt(args[3]);
    Map<String, Integer> topicMap = new HashMap();
    //String[] topics = args[2].split(",");
    String topic = "test";
    //for (String topic: topics) {
      topicMap.put(topic, numThreads);
    //}

    JavaPairReceiverInputDStream<String, String> messages =
            KafkaUtils.createStream(jssc, "localhost:2181", "test", topicMap);

    JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
      public String call(Tuple2<String, String> tuple2) {
        return tuple2._2();
      }
    });
    
    JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
    	public Iterable<String> call(String x) {
      	  String received = x.substring(x.indexOf(":")+1);
      	  String lightString;
      	  System.out.println("Light Value: "+received);
      	  float value = Float.valueOf(received);
      	  	if(value < 500) {
      	  	lightString = "Dim";
      	  	} else if (value < 1500){
      	  		lightString = "Normal";
      	  	} else if (value < 2500) {
      	  		lightString = "Bright";
      	  	} else {
      	  		lightString = "Blinding";
      	  	}
      	  //return Arrays.asList(x.split(" "));
      	  	KafkaP.sendMessageReply(lightString);
      	  	return Arrays.asList(lightString);
        }
    });
    //System.out.println(words.toString() + "sysoyt");
    words.print();
    
    //messages.print();

    /*JavaPairDStream<String, Integer> wordCounts = words.mapToPair(
      new PairFunction<String, String, Integer>() {
        public Tuple2<String, Integer> call(String s) {
          return new Tuple2(s, 1);
        }
      }).reduceByKey(new Function2<Integer, Integer, Integer>() {
        public Integer call(Integer i1, Integer i2) {
          return i1 + i2;
        }
      });

    wordCounts.print();*/
    jssc.start();
    jssc.awaitTermination();
  }
}