package com.sharingmap.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "item_images")
class ItemImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "item_image_generator")
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="item_id", nullable = false)
    @JsonIgnore
//    @Column(name = "entity_id", unique = false, nullable = false)
    var entity: ItemEntity? = null
) {
    @CreationTimestamp
    var createdDate: LocalDateTime? = null

    @UpdateTimestamp
    var updateDate: LocalDateTime? = null

    var isLoaded: Boolean? = false
}

@Entity
@Table(name = "user_images")
class UserImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "item_image_generator")
    var id: UUID? = null,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="entity_id", nullable = false)
    @JsonIgnore
    var entity: UserEntity? = null
) {
    @CreationTimestamp
    var createdDate: LocalDateTime? = null

    @UpdateTimestamp
    var updateDate: LocalDateTime? = null

    var isLoaded: Boolean? = false
}