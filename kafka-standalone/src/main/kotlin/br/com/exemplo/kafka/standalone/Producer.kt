package br.com.exemplo.kafka.standalone

import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*
import java.io.IOException
import org.apache.kafka.clients.producer.ProducerRecord

class Producer {

    @Throws(IOException::class)
    fun produce(brokers: String, topicName: String) {

        val producer = KafkaProducer<String, String>(getProperties(brokers))

        repeat(100) {
            val sentence = getRandomSentence()

            try {
                producer.send(ProducerRecord<String, String>(topicName, sentence)).get()
            } catch (ex: Exception) {
                throw IOException(ex)
            }
        }

        println("Finished")
    }

    private fun getProperties(brokers: String) = Properties().apply {
        setProperty("bootstrap.servers", brokers)
        setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }

    private fun getRandomSentence() = sentences[Random().nextInt(sentences.size)]

    private val sentences = arrayOf(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
        "Phasellus bibendum urna a tristique congue",
        "Duis venenatis nunc a nisi tempus, vel pretium odio finibus",
        "Sed fermentum neque sed elit viverra condimentum",
        "Aliquam semper nunc ut sapien elementum iaculis",
        "Sed sit amet nibh eu justo viverra mattis",
        "Nulla porta odio ac sodales elementum",
        "Sed elementum nisl in lorem ornare, eu volutpat dolor convallis",
        "Vivamus non tortor sit amet erat pretium congue a at risus",
        "Etiam lobortis magna sit amet libero consequat, vel tincidunt elit interdum",
        "Cras aliquam odio eu libero finibus laoreet",
        "Nam sed lacus in elit tempus faucibus",
        "Cras in ex eu purus porta blandit id sit amet purus",
        "Sed fringilla sapien vel nisi vestibulum ornare",
        "Nullam facilisis urna vel blandit tempus",
        "Suspendisse nec turpis volutpat, varius sem at, consectetur mi"
    )

}

