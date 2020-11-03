package org.example.Stream101;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

public class FavouriteColorApp {

    /*
# create input topic
   kafka-topics --zookeeper 127.0.0.1:2181 --topic favourite-color-input --create --partitions 2 --replication-factor 1

# create output topic
   kafka-topics --zookeeper 127.0.0.1:2181 --topic favourite-color-output --create --partitions 2 --replication-factor 1

# launch a Kafka consumer
   kafka-console-consumer --bootstrap-server 127.0.0.1:9092 \
           --topic favourite-color-output \
           --from-beginning \
           --formatter kafka.tools.DefaultMessageFormatter \
           --property print.key=true \
           --property print.value=true \
           --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
           --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

# launch the streams application in Intellij

# produce data
   kafka-console-producer --broker-list 127.0.0.1:9092 --topic favourite-color-input
*/
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        FavouriteColorApp favouriteColorApp = new FavouriteColorApp();

        KafkaStreams streams = new KafkaStreams(favouriteColorApp.createTopology(), config);
        streams.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        // Update:
        // print the topology every 10 seconds for learning purposes
        while (true) {
            streams.localThreadsMetadata().forEach(data -> System.out.println(data));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        return builder.build();
    }
}
