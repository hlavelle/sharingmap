package com.sharingmap.adresses

import java.time.LocalDateTime
import java.util.UUID

data class AddressDto(
    val id: UUID? = null,
    val name: String,
    val description: String? = null,
    val locationIds: Set<Long>,
    val cityId: Long,
    val userId: UUID? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

data class CreateAddressDto(
    val name: String,
    val description: String? = null,
    val locationIds: Set<Long>,
    val cityId: Long
) {
    init {
        require(locationIds.isNotEmpty()) { "At least one location must be provided" }
    }
}

data class UpdateAddressDto(
    val name: String? = null,
    val description: String? = null,
    val locationIds: Set<Long>? = null,
    val cityId: Long? = null
) {
    init {
        locationIds?.let {
            require(it.isNotEmpty()) { "If locationIds is provided, at least one location must be included" }
        }
    }
}

data class AddressResponseDto(
    val id: UUID,
    val name: String,
    val description: String?,
    val locationIds: Set<Long>,
    val cityId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val user_id: UUID
)