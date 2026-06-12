package com.sharingmap.notifications

import com.google.firebase.messaging.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class NotificationService(
    private val fcmTokenRepository: FcmTokenRepository
) {
    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    companion object {
        private const val FCM_MULTICAST_LIMIT = 500
        private val STALE_ERROR_CODES = setOf(
            MessagingErrorCode.UNREGISTERED,
            MessagingErrorCode.INVALID_ARGUMENT
        )
    }

    @Transactional
    fun registerToken(userId: UUID, token: String, platform: String) {
        val existing = fcmTokenRepository.findByToken(token)

        if (existing != null) {
            if (existing.userId != userId) {
                fcmTokenRepository.deleteByToken(token)
                fcmTokenRepository.flush()
                fcmTokenRepository.save(
                    FcmTokenEntity(userId = userId, token = token, platform = platform)
                )
                log.info("Token reassigned from user {} to {}", existing.userId, userId)
            } else {
                existing.updatedAt = Instant.now()
                log.debug("Token refreshed for user {}", userId)
            }
        } else {
            fcmTokenRepository.save(
                FcmTokenEntity(userId = userId, token = token, platform = platform)
            )
            log.info("New token registered for user {}", userId)
        }
    }

    @Transactional
    fun unregisterToken(token: String) {
        fcmTokenRepository.deleteByToken(token)
    }

    @Transactional
    fun unregisterAllTokens(userId: UUID) {
        fcmTokenRepository.deleteAllByUserId(userId)
    }

    // ──────────────── Sending ────────────────

    fun sendToUser(
        userId: UUID,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): Int {
        val tokens = fcmTokenRepository.findAllByUserId(userId)
        if (tokens.isEmpty()) {
            log.warn("No tokens found for user {}", userId)
            return 0
        }
        return sendMulticast(tokens, title, body, data).also {
            log.info("Sent to user {}: {}/{} successful", userId, it, tokens.size)
        }
    }

    fun sendToToken(
        token: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): String {
        val message = Message.builder()
            .setNotification(buildNotification(title, body))
            .putAllData(data)
            .setToken(token)
            .build()

        return FirebaseMessaging.getInstance().send(message)
    }

    fun sendToTopic(
        topic: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): String {
        val message = Message.builder()
            .setNotification(buildNotification(title, body))
            .putAllData(data)
            .setTopic(topic)
            .build()

        return FirebaseMessaging.getInstance().send(message)
    }

    fun sendToUsers(
        userIds: Collection<UUID>,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): Int {
        if (userIds.isEmpty()) return 0

        val tokens = fcmTokenRepository.findAllByUserIdIn(userIds)
        if (tokens.isEmpty()) return 0

        return sendMulticast(tokens, title, body, data).also {
            log.info("Broadcast to {} users, {} tokens delivered", userIds.size, it)
        }
    }

    // ──────────────── Private helpers ────────────────

    private fun buildNotification(title: String, body: String): Notification =
        Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build()

    private fun sendMulticast(
        tokens: List<FcmTokenEntity>,
        title: String,
        body: String,
        data: Map<String, String>
    ): Int {
        var totalSuccess = 0

        tokens.chunked(FCM_MULTICAST_LIMIT).forEach { chunk ->
            val message = MulticastMessage.builder()
                .setNotification(buildNotification(title, body))
                .putAllData(data)
                .addAllTokens(chunk.map { it.token })
                .build()

            val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)
            cleanupStaleTokens(chunk, response)
            totalSuccess += response.successCount
        }

        return totalSuccess
    }

    @Transactional
    fun cleanupStaleTokens(tokens: List<FcmTokenEntity>, response: BatchResponse) {
        val staleTokens = response.responses
            .mapIndexedNotNull { i, sendResponse ->
                if (!sendResponse.isSuccessful) {
                    val code = (sendResponse.exception)
                        ?.messagingErrorCode
                    if (code in STALE_ERROR_CODES) tokens[i].token else null
                } else null
            }

        if (staleTokens.isNotEmpty()) {
            fcmTokenRepository.deleteAllByTokenIn(staleTokens)
            log.info("Cleaned up {} stale tokens", staleTokens.size)
        }
    }
}