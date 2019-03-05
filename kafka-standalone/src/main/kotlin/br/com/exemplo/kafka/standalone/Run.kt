package br.com.exemplo.kafka.standalone

import java.util.UUID

/*
 * Origem: https://github.com/Azure-Samples/hdinsight-kafka-java-get-started
 */
fun main(args: Array<String>) {

    if (args.size < 3) {
        usage()
    }

    // Get the brokers
    val brokers = args[2]
    val topicName = args[1]
    when (args[0].toLowerCase()) {
        "producer" -> Producer().produce(brokers, topicName)
        "consumer" -> {
            val groupId = if (args.size == 4) {
                args[3]
            } else {
                UUID.randomUUID().toString()
            }

            Consumer().consume(brokers, groupId, topicName)
        }
        "describe" -> AdminClientWrapper().describeTopics(brokers, topicName)
        "create" -> AdminClientWrapper().createTopics(brokers, topicName)
        "delete" -> AdminClientWrapper().deleteTopics(brokers, topicName)
        else -> usage()
    }
    System.exit(0)
}

// Display usage
fun usage() {
    println("Usage:")
    println("kafka-example.jar <producer|consumer|describe|create|delete> <topicName> brokerhosts [groupid]")
    System.exit(1)
}
