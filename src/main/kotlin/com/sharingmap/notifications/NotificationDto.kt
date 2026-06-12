package com.sharingmap.notifications

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class DeviceTokenRequest(
    @field:NotBlank
    @field:Size(max = 512)
    val token: String,

    @field:NotBlank
    val platform: String   // "android" | "ios" | "web"
)

data class SendNotificationRequest(
    val userId: UUID,
    @field:NotBlank val title: String,
    @field:NotBlank val body: String,
    val data: Map<String, String> = emptyMap()
)

data class SendToTokenRequest(
    @field:NotBlank val token: String,
    @field:NotBlank val title: String,
    @field:NotBlank val body: String,
    val data: Map<String, String> = emptyMap()
)

data class TopicNotificationRequest(
    @field:NotBlank val topic: String,
    @field:NotBlank val title: String,
    @field:NotBlank val body: String,
    val data: Map<String, String> = emptyMap()
)