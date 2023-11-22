package com.sharingmap.contact

import com.sharingmap.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "contacts")
class ContactEntity(
    var contact: String,
    var type: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "contact_generator")
    var id: UUID? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}