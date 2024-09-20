package com.sharingmap.location

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sharingmap.annotations.MyMockable
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
@MyMockable
@Table(name = "locations")
class LocationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

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
    val items: Set<ItemEntity> = HashSet(),

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
)