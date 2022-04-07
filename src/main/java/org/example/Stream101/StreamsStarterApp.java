package org.example.Stream101;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;

public class StreamsStarterApp {

  public static void main(String[] args) {
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-starter-app");
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    // 1 - stream from Kafka

    KStream<String, String> textLines = builder.stream("first_topic");
//    var test = textLines
//        .filter((key, value) -> value.length() > 0)
//        .mapValues(v -> String.valueOf(Integer.parseInt(v) * 2));
    KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), config);
    kafkaStreams.start();
  }
}
