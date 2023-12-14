package com.sharingmap.location

import com.sharingmap.city.CityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LocationRepository: JpaRepository<LocationEntity, Long> {
    fun findAllByCityOrderByNameAsc(city: CityEntity): List<LocationEntity>
}