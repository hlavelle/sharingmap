package com.sharingmap.item

import com.sharingmap.category.CategoryEntity
import com.sharingmap.city.CityEntity
import com.sharingmap.contact.ContactEntity
import com.sharingmap.image.ItemImageEntity
import com.sharingmap.subcategory.SubcategoryEntity
import com.sharingmap.user.UserEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "items")
class ItemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "item_generator")
    var id: UUID? = null,

    @Column(name = "item_name")
    @get:Size(min = 3, max = 50)
    var name: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    var category: CategoryEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    var subcategory: SubcategoryEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    var city: CityEntity? = null,

    var text: String? = null,

    var address: String? = null,

    @field:Pattern(regexp = "(^$|[0-9]{10})")
    var phoneNumber: String? = null,

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "contact_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    var contact: ContactEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity? = null
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy="entity")
    val images: List<ItemImageEntity>? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}