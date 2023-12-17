package com.sharingmap.location

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sharingmap.city.CityEntity
import com.sharingmap.item.ItemEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "locations")
class LocationEntity(
    @Column(name = "location_name", unique = false)
    var name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: LocationType,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var city: CityEntity,

    @JsonIgnore
    @ManyToMany(mappedBy = "locations")
    val items: Set<ItemEntity> = HashSet()

) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_generator")
    var id: Long? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
}