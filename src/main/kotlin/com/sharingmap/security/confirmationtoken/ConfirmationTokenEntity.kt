package com.sharingmap.security.confirmationtoken

import com.sharingmap.entities.UserEntity
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
    var token: String? = null,

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: LocalDateTime? = null,

    @Column(nullable = false)
    var expiresAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "token_generator")
    var id: UUID? = null,
    )