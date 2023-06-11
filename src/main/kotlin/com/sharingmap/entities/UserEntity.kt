package com.sharingmap.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    var id: Long? = null

    @Column(name = "username", nullable = false, unique = true, length = 20)
    var name: String? = null

    //    var profilePicture: ImageEntity? = null

    //    @Lob @Type(type = "org.hibernate.type.TextType")
    @get:Size(min=20, max=300)
    var bio: String? = null

    @field:Email(message = "{validation.field.email.invalid-format}")
    var email: String? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

}