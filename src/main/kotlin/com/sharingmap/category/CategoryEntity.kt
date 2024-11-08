package com.sharingmap.category

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sharingmap.annotations.Mockable
import com.sharingmap.item.ItemEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Mockable
@Table(name = "categories")
class CategoryEntity(
    @Column(name = "category_name", unique = true)
    var name: String,

    var description: String,

    @JsonIgnore
    @ManyToMany(mappedBy = "categories", targetEntity = ItemEntity::class)
    val items: Set<ItemEntity> = HashSet()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}
