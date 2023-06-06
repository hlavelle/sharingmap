package com.sharingmap.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "images")
class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_generator")
    var id: Long? = null

    @Column(name = "image_name")
    var name: String? = null

    var url: String? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}