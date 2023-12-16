package com.sharingmap.category

import com.sharingmap.item.ItemEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "categories")
class CategoryEntity(
    @Column(name = "category_name", unique = true)
    var name: String,

    var description: String,

    @ManyToMany(mappedBy = "categories")
    val items: Set<ItemEntity> = HashSet()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    var id: Long? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}
