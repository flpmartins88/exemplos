package br.com.empresa.kafka.rest

import br.com.empresa.kafka.MessageProducerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/message")
class MessageController(private val messageProducerService: MessageProducerService) {

    @PostMapping
    fun create(@RequestBody content: MessageExchange.Request): ResponseEntity<Unit> {
        messageProducerService.send(content.message)
        return ResponseEntity.ok().build<Unit>()
    }

}