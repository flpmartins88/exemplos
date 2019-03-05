package br.com.exemplo.kafka.standalone

import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import java.io.IOException
import java.util.*


class AdminClientWrapper {

    fun getProperties(brokers: String): Properties {
        val properties = Properties()
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)

        // Set how to serialize key/value pairs
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
        // specify the protocol for Domain Joined clusters
        //properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");

        return properties
    }

    @Throws(IOException::class)
    fun describeTopics(brokers: String, topicName: String) {
        // Set properties used to configure admin client
        val properties = getProperties(brokers)

        try {
            KafkaAdminClient.create(properties).use { adminClient ->
                // Make async call to describe the topic.
                val describeTopicsResult = adminClient.describeTopics(Collections.singleton(topicName))

                val description = describeTopicsResult.values().get(topicName)?.get()
                print(description.toString())
            }
        } catch (e: Exception) {
            println("Describe denied    ")
            println(e.message)
            //throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Throws(IOException::class)
    fun deleteTopics(brokers: String, topicName: String) {
        // Set properties used to configure admin client
        val properties = getProperties(brokers)

        try {
            KafkaAdminClient.create(properties).use { adminClient ->
                val deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicName))
                deleteTopicsResult.values().get(topicName)?.get()
                print("Topic $topicName deleted")
            }
        } catch (e: Exception) {
            print("Delete Topics denied\n")
            print(e.message)
            //throw new RuntimeException(e.getMessage(), e);
        }

    }

    @Throws(IOException::class)
    fun createTopics(brokers: String, topicName: String) {
        // Set properties used to configure admin client
        val properties = getProperties(brokers)

        try {
            KafkaAdminClient.create(properties).use { adminClient ->
                val numPartitions = 1
                val replicationFactor = 1.toShort()
                val newTopic = NewTopic(topicName, numPartitions, replicationFactor)

                val createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic))
                createTopicsResult.values().get(topicName)?.get()
                print("Topic $topicName created")
            }
        } catch (e: Exception) {
            println("Create Topics denied")
            println(e.message)
            //throw new RuntimeException(e.getMessage(), e);
        }

    }

}