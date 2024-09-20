package com.sharingmap.subcategory

import com.sharingmap.annotations.MyMockable
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


@Entity
@MyMockable
@Table(name = "subcategories")
class SubcategoryEntity (
    @Column(name = "category_name", unique = true)
    var name: String,

    var description: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subcategory_generator")
    var id: Long? = null

    var imageUrl: String? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}
