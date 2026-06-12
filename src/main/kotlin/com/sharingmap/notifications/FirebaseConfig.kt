package com.sharingmap.notifications

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfig(
    @Value("\${firebase.service-account-path:firebase-service-account.json}")
    private val serviceAccountPath: String,

    @Value("\${firebase.project-id:sharingmap-9ad66}")
    private val expectedProjectId: String
) {
    private val log = LoggerFactory.getLogger(FirebaseConfig::class.java)

    @PostConstruct
    fun initialize() {
        if (FirebaseApp.getApps().isNotEmpty()) {
            log.info("FirebaseApp already initialized, skipping")
            return
        }

        val resource = ClassPathResource(serviceAccountPath)
        require(resource.exists()) {
            "Firebase service account file not found at classpath:$serviceAccountPath"
        }

        resource.inputStream.use { stream ->
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(
                    listOf(
                        "https://www.googleapis.com/auth/firebase.messaging",
                        "https://www.googleapis.com/auth/cloud-platform"
                    )
                )

            if (credentials is ServiceAccountCredentials) {
                log.info("Service Account Email: {}", credentials.clientEmail)
                log.info("Project ID from JSON:  {}", credentials.projectId)

                require(credentials.projectId == expectedProjectId) {
                    "Project ID mismatch: JSON='${credentials.projectId}', expected='$expectedProjectId'"
                }
            }

            // ── Fail-fast: try to actually call FCM right now ──
            try {
                credentials.refreshIfExpired()
                val token = credentials.accessToken.tokenValue
                log.info("Access token obtained (first 20 chars): {}...", token.take(20))

                // Actually test the FCM endpoint
                testFcmEndpoint(token, expectedProjectId)
            } catch (e: Exception) {
                log.error("Startup FCM check failed", e)
                throw e
            }

            val options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(expectedProjectId)
                .build()

            FirebaseApp.initializeApp(options)
            log.info("FirebaseApp initialized for project '{}'", expectedProjectId)
        }
    }

    /**
     * Sends a dummy request to FCM to verify the API is enabled.
     * A 400 (INVALID_ARGUMENT) is actually SUCCESS — it means auth worked.
     * A 401 means the API is not enabled or credentials are wrong.
     */
    private fun testFcmEndpoint(accessToken: String, projectId: String) {
        val url = java.net.URI(
            "https://fcm.googleapis.com/v1/projects/$projectId/messages:send"
        ).toURL()

        val conn = url.openConnection() as java.net.HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Authorization", "Bearer $accessToken")
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        val body = """
            {
              "message": {
                "token": "startup-connectivity-test",
                "notification": {"title":"test","body":"test"}
              }
            }
        """.trimIndent()

        conn.outputStream.use { it.write(body.toByteArray()) }

        val code = conn.responseCode
        val responseBody = try {
            (if (code < 400) conn.inputStream else conn.errorStream)
                ?.bufferedReader()?.readText() ?: "(empty)"
        } catch (_: Exception) { "(unreadable)" }

        conn.disconnect()

        when (code) {
            200, 400 -> {
                // 400 = INVALID_ARGUMENT for fake token, which means AUTH WORKED
                log.info("FCM connectivity test passed (HTTP {})", code)
            }
            401 -> {
                log.error("FCM returned 401. Response: {}", responseBody)
                log.error("═══════════════════════════════════════════════════════")
                log.error("  The FCM v1 API is NOT ENABLED for project '{}'", projectId)
                log.error("  Enable it at:")
                log.error("  https://console.cloud.google.com/apis/library/fcm.googleapis.com?project={}", projectId)
                log.error("═══════════════════════════════════════════════════════")
                throw IllegalStateException(
                    "FCM API not enabled for project '$projectId'. " +
                            "Enable at: https://console.cloud.google.com/apis/library/fcm.googleapis.com?project=$projectId"
                )
            }
            403 -> {
                log.error("FCM returned 403 (permission denied). Response: {}", responseBody)
                throw IllegalStateException("FCM returned 403: $responseBody")
            }
            else -> {
                log.warn("FCM connectivity test returned unexpected HTTP {}: {}", code, responseBody)
            }
        }
    }
}