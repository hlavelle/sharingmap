package com.sharingmap.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


@Entity
@Table(name = "items")
class ItemEntity (
    @Column(name = "item_name")
    @get:Size(min = 3, max = 50)
    var name: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    var category: CategoryEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subcategory_id", nullable = false)
    @JsonIgnore
    var subcategory: SubcategoryEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    var city: CityEntity? = null,

    @get:Size(min = 20, max = 300)
    var text: String? = null,

    var address: String? = null,

    @field:Pattern(regexp = "(^$|[0-9]{10})")
    var phoneNumber: String? = null,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    var user: UserEntity? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
    var id: Long? = null
}

//{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
//    var id: Long? = null
//
//    @Column(name = "item_name")
//    @get:Size(min=3, max=50)
//    var name: String? = null
//
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "category_id", nullable = false)
//    //@JsonIgnore
//    var category: CategoryEntity? = null
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "subcategory_id", nullable = false)
//    //@JsonIgnore
//    var subcategory: SubcategoryEntity? = null
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "city_id", nullable = false)
//    //@JsonIgnore
//    var city: CityEntity? = null
////
//////    var photo: ImageEntity? = null
////
//////    @Lob @Type(type = "org.hibernate.type.TextType")
//    @get:Size(min=20, max=300)
//    var text: String? = null
//
//    var address: String? = null
//
//    @field:Pattern(regexp="(^$|[0-9]{10})")
//    var phoneNumber: String? = null
//
//    //способ связи
//
//    //var status: Boolean = true //актуальность объявления, лучше сделать категории "в обработке", "актуальный", "архив"
//
//    //дата удаления, если объявление еще актуально. Рассчитывать с даты апдейта
//
//    @CreationTimestamp
//    var createdAt: LocalDateTime? = null
//
//    @UpdateTimestamp
//    var updatedAt: LocalDateTime? = null
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    //@JsonIgnore
//    var user: UserEntity? = null
//}