package br.com.empresa.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducerService(private val kafkaTemplate: KafkaTemplate<String, String>) {

    fun send(message: String) {
        this.kafkaTemplate.send("test", message)
    }

}
