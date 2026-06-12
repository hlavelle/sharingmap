package com.sharingmap.adresses

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AddressRepository : JpaRepository<AddressEntity, UUID> {
    fun findAllByUserIdAndState(userId: UUID, state: AddressState): List<AddressEntity>
    fun existsByIdAndUserIdAndState(addressId: UUID, userId: UUID, state: AddressState): Boolean
}