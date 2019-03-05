package br.com.empresa.kafka

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class MessageConsumerService {

    companion object {
        val log = LoggerFactory.getLogger(MessageConsumerService::class.java)!!
    }

    @KafkaListener(topics = ["test"])
    fun process(message: String) {
        log.info("Consumiu: $message")
    }

}