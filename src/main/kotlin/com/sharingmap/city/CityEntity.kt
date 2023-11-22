package com.sharingmap.city

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "cities")
class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_generator")
    var id: Long? = null

    @Column(name = "city_name", unique = true)
    var name: String? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}