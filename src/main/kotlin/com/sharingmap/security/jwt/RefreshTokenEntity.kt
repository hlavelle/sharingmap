package com.sharingmap.security.jwt

import com.sharingmap.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.Instant
import java.util.*

@Entity
@Table(name = "refreshtokens")
public class RefreshTokenEntity (
    @Column(nullable = false, unique = true)
    val token: String,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity,

    @Column(nullable = false)
    val expiryDate: Instant,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "user_generator")
    var id: UUID? = null)
