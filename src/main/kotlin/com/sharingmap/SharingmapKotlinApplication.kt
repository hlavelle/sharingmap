package com.sharingmap

import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SharingmapKotlinApplication

fun main(args: Array<String>) {
    runApplication<SharingmapKotlinApplication>(*args)
}
//
//@RestController
//class MessageResource(val service: MessageService) {
//    @GetMapping("/")
//    fun index(): List<Message> = service.findMessages()
//
//    @PostMapping("/")
//    fun post(@RequestBody message: Message) {
//        service.post(message)
//    }
//}
//
//@Service
//class MessageService(val db: MessageRepository) {
//    fun findMessages(): List<Message> = db.findMessages()
//
//    fun post(message: Message) {
//        db.save(message)
//    }
//}
//
//interface MessageRepository : JpaRepository<Message, String>{
//    @Query("select * from MESSAGES", nativeQuery=true)
//    fun findMessages(): List<Message>
//}
//
//@Table(name = "MESSAGES")
//data class Message(@Id val id: String?, val text: String)