package com.sharingmap

import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.sharingmap.*"))
class SharingmapKotlinApplication

fun main(args: Array<String>): Unit = runBlocking {
    runApplication<SharingmapKotlinApplication>(*args)
}

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