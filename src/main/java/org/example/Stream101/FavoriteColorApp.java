package org.example.Stream101;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class FavoriteColorApp {

    /*
# create input topic
   kafka-topics --bootstrap-server localhost:9092 --topic favorite-color-input --create --partitions 2 --replication-factor 1
# create middle layer topic
   kafka-topics --bootstrap-server localhost:9092 --topic users-and-colors --create --partitions 2 --replication-factor 1

# create output topic
   kafka-topics --bootstrap-server localhost:9092 --topic favorite-color-output --create --partitions 2 --replication-factor 1

# launch a Kafka consumer
   kafka-console-consumer --bootstrap-server 127.0.0.1:9092 \
           --topic favorite-color-output \
           --from-beginning \
           --formatter kafka.tools.DefaultMessageFormatter \
           --property print.key=true \
           --property print.value=true \
           --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
           --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

# launch the streams application in Intellij

# produce data
   kafka-console-producer --broker-list 127.0.0.1:9092 --topic favorite-color-input
   stephane,blue
   john,green
   stephane,red
   alice,red


*/

    private Topology createTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("favorite-color-input");
        KStream<String, String> usersAndColors = textLines
                .filter((key, value) -> value.contains(","))
                .selectKey((key, value) -> value.split(",")[0].toLowerCase())
                .mapValues((key, value) -> value.split(",")[1].toLowerCase())
                .filter((user, color) -> Arrays.asList("green", "blue", "red").contains(color));

        usersAndColors.to("users-and-colors");

        KTable<String, String> usersAndColorsTable = builder.table("users-and-colors");

        KTable<String, Long> colorCountTable = usersAndColorsTable
                .groupBy((user, color) -> new KeyValue<>(color, color))
                .count(Materialized.as("CountsByColors"));

        colorCountTable.toStream().to("favorite-color-output", Produced.with(Serdes.String(), Serdes.Long()));
        return builder.build();
    }

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "favorite-color-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        FavoriteColorApp favoriteColorApp = new FavoriteColorApp();

        KafkaStreams streams = new KafkaStreams(favoriteColorApp.createTopology(), config);
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

}
