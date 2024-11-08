package com.sharingmap.subcategory

import com.sharingmap.annotations.Mockable
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


@Entity
@Mockable
@Table(name = "subcategories")
class SubcategoryEntity (
    @Column(name = "category_name", unique = true)
    var name: String,

    var description: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    var imageUrl: String? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}
