# kafka-streams
projects to practice Kafka Stream

## scripts for run Word-Count
```
# launch zookeeper
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

# launch kafka instance
kafka-server-start /usr/local/etc/kafka/server.properties

# create input topic
kafka-topics --zookeeper 127.0.0.1:2181 --topic word-count-input --create --partitions 2 --replication-factor 1

# create output topic
kafka-topics --zookeeper 127.0.0.1:2181 --topic word-count-output --create --partitions 2 --replication-factor 1

# launch a Kafka consumer
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 \
	--topic word-count-output \
	--from-beginning \
	--formatter kafka.tools.DefaultMessageFormatter \
	--property print.key=true \
	--property print.value=true \
	--property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
	--property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

# launch the streams application in Intellij

# produce data
kafka-console-producer --broker-list 127.0.0.1:9092 --topic word-count-input

# list all topics that we have in Kafka
kafka-topics --zookeeper 127.0.0.1:2181 --list
```
