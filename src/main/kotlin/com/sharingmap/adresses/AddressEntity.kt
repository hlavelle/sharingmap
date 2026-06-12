package com.sharingmap.adresses

import com.sharingmap.annotations.Mockable
import com.sharingmap.city.CityEntity
import com.sharingmap.location.LocationEntity
import com.sharingmap.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import java.util.*

enum class AddressState {
    ACTIVE,
    DELETED
}

@Entity
@Mockable
@Table(name = "addresses")
class AddressEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(name = "address_name", nullable = false)
    var name: String,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: UserEntity,

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = LocationEntity::class)
    @JoinTable(
        name = "address_locations",
        joinColumns = [JoinColumn(name = "address_id")],
        inverseJoinColumns = [JoinColumn(name = "location_id")]
    )
    var locations: Set<LocationEntity> = emptySet(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = CityEntity::class)
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var city: CityEntity,

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    @ColumnDefault("'ACTIVE'")
    var state: AddressState = AddressState.ACTIVE,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)