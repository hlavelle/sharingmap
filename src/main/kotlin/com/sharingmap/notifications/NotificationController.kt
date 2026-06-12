package com.sharingmap.notifications

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {
    @PostMapping("/tokens")
    fun registerToken(
        @RequestHeader("X-User-Id") userId: UUID,
        @Valid @RequestBody request: DeviceTokenRequest
    ): ResponseEntity<Map<String, String>> {
        notificationService.registerToken(userId, request.token, request.platform)
        return ResponseEntity.ok(mapOf("status" to "registered"))
    }

    @DeleteMapping("/tokens/{token}")
    fun unregisterToken(
        @RequestHeader("X-User-Id") userId: UUID,
        @PathVariable token: String
    ): ResponseEntity<Map<String, String>> {
        notificationService.unregisterToken(token)
        return ResponseEntity.ok(mapOf("status" to "removed"))
    }

    @PostMapping("/send/user")
    fun sendToUser(
        @Valid @RequestBody request: SendNotificationRequest
    ): ResponseEntity<Map<String, Any>> {
        val count = notificationService.sendToUser(
            request.userId, request.title, request.body, request.data
        )
        return ResponseEntity.ok(mapOf("successCount" to count))
    }

    @PostMapping("/send/topic")
    fun sendToTopic(
        @Valid @RequestBody request: TopicNotificationRequest
    ): ResponseEntity<Map<String, String>> {
        val messageId = notificationService.sendToTopic(
            request.topic, request.title, request.body, request.data
        )
        return ResponseEntity.ok(mapOf("messageId" to messageId))
    }

    @PostMapping("/send/token")
    fun sendToToken(
        @Valid @RequestBody request: SendToTokenRequest
    ): ResponseEntity<Map<String, String>> {
        val messageId = notificationService.sendToToken(
            request.token, request.title, request.body, request.data
        )
        return ResponseEntity.ok(mapOf("messageId" to messageId))
    }
}