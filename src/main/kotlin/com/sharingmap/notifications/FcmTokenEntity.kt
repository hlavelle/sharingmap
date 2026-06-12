package com.sharingmap.notifications

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "fcm_tokens",
    indexes = [Index(name = "idx_fcm_tokens_user_id", columnList = "user_id")],
    uniqueConstraints = [UniqueConstraint(name = "uq_fcm_tokens_token", columnNames = ["token"])]
)
class FcmTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false, length = 512)
    val token: String,

    @Column(nullable = false, length = 16)
    val platform: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
)