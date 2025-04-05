package com.sharingmap.image

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sharingmap.annotations.Mockable
import com.sharingmap.item.ItemDto
import com.sharingmap.item.ItemEntity
import com.sharingmap.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

enum class ImageResolution {
    SMALL, LOW, NORMAL, HIGH
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class BaseImageEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: UUID = UUID.randomUUID(),

    @CreationTimestamp
    open var createdDate: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    var updateDate: LocalDateTime = LocalDateTime.now(),

    open var isLoaded: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution", nullable = false)
    open var resolution: ImageResolution = ImageResolution.NORMAL
)

@Entity
@Mockable
@Table(name = "item_images")
class ItemImageEntity(
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = ItemEntity::class)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    var entity: ItemEntity,

    id: UUID = UUID.randomUUID()
) : BaseImageEntity(id = id)

@Entity
@Table(name = "user_images")
class UserImageEntity(
    @OneToOne(fetch = FetchType.LAZY, optional = false, targetEntity = UserEntity::class)
    @JoinColumn(name = "entity_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    var entity: UserEntity,

    id: UUID = UUID.randomUUID()
) : BaseImageEntity(id = id)
