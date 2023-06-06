package com.sharingmap.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


@Entity
@Table(name = "items")
class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_generator")
    var id: Long? = null

    @Column(name = "item_name")
    var name: String? = null


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    var category: CategoryEntity? = null

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "subcategory_id", nullable = false)
//    @JsonIgnore
//    private var subCategories: HashSet<SubcategoryEntity> = HashSet()

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnore
    var city: CityEntity? = null
//
////    var photo: ImageEntity? = null
//
////    @Lob @Type(type = "org.hibernate.type.TextType")
//    var text: String? = null
//
//    var address: String? = null

    // номер телефона
    //способ связи


    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    var user: UserEntity? = null
}