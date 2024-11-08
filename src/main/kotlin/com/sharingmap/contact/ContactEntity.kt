package com.sharingmap.contact

import com.sharingmap.annotations.Mockable
import com.sharingmap.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Mockable
@Table(name = "contacts")
class ContactEntity(
    var contact: String, //TODO validation of phone number or telegram nickname

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: TypeContact,

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = UserEntity::class)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}