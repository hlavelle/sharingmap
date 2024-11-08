package com.sharingmap.security.confirmationtoken

import com.sharingmap.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tokens")
class ConfirmationTokenEntity(

    @Column(nullable = false)
    var token: String,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: LocalDateTime? = null,

    @Column(nullable = false)
    var expiresAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = UserEntity::class)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
    )