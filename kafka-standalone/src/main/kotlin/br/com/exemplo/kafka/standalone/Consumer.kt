package br.com.exemplo.kafka.standalone

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.*


class Consumer {

    fun consume(brokers: String, groupId: String, topicName: String): Int {

        val consumer = KafkaConsumer<String, String>(getProperties(brokers, groupId))
        consumer.subscribe(Arrays.asList(topicName))

        var count = 0
        while (true) {
            val records = consumer.poll(Duration.ofMillis(200))

            if (records.count() == 0) continue

            records.forEach { record ->
                println("${++count}: ${record.value()}")
            }

            //consumer.commitSync()
        }
    }

    private fun getProperties(brokers: String, groupId: String) = Properties().apply {
        setProperty("bootstrap.servers", brokers)
        setProperty("group.id", groupId)
        setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        // if you sets autocommit as false, you must commit after read
        //setProperty("auto.offset.commit", "false")
        // When a group is first created, it has no offset stored to start reading from. This tells it to start
        // with the earliest record in the stream.
        //setProperty("auto.offset.reset", "earliest")
    }
}