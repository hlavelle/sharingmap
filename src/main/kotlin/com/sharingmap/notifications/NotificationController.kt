package com.sharingmap.notifications

import com.sharingmap.user.UserEntity
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * Device-token registry and send API for push notifications.
 *
 * ⚠ **The feature is parked** (decision D4): no device registers a token, nothing triggers
 * a send, and `FirebaseConfig` differs between the working tree and `HEAD`
 * (known-issues #5). The endpoints are kept and secured rather than deleted.
 *
 * Identity comes from the authenticated principal. It used to come from an `X-User-Id`
 * **request header** on routes that had no security matcher at all — so anyone could bind
 * a device token to any account, unregister anyone's device, or push to any user or topic.
 * See known-issues #4.
 */
@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {
    /** Registers a device token for the **calling** user. */
    @PostMapping("/tokens")
    fun registerToken(
        @AuthenticationPrincipal user: UserEntity,
        @Valid @RequestBody request: DeviceTokenRequest
    ): ResponseEntity<Map<String, String>> {
        notificationService.registerToken(user.id, request.token, request.platform)
        return ResponseEntity.ok(mapOf("status" to "registered"))
    }

    /** Removes one of the **calling** user's own device tokens. */
    @DeleteMapping("/tokens/{token}")
    fun unregisterToken(
        @AuthenticationPrincipal user: UserEntity,
        @PathVariable token: String
    ): ResponseEntity<Map<String, String>> {
        notificationService.unregisterToken(user.id, token)
        return ResponseEntity.ok(mapOf("status" to "removed"))
    }

    // ── /send/** is ROLE_ADMIN in SecurityConfig: these are operational tools, not
    //    something a user may invoke. Sending is not rate-limited or audited.

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