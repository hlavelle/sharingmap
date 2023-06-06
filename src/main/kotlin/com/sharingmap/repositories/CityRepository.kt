package com.sharingmap.repositories

import com.sharingmap.entities.CityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CityRepository : JpaRepository<CityEntity, Long> {
}