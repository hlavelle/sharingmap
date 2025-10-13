package com.sharingmap.adresses

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AddressRepository : JpaRepository<AddressEntity, UUID> {
    fun findAllByUserId(userId: UUID): List<AddressEntity>
    fun existsByIdAndUserId(addressId: UUID, userId: UUID): Boolean
}