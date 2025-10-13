package com.sharingmap.adresses

import java.util.UUID

interface AddressService {
    fun findAllByUserId(userId: UUID): List<AddressResponseDto>
    fun findById(id: UUID): AddressEntity
    fun createAddress(userId: UUID, createAddressDto: CreateAddressDto): AddressResponseDto
    fun updateAddress(id: UUID, updateAddressDto: UpdateAddressDto): AddressResponseDto
    fun deleteAddress(id: UUID)
    fun isAddressOwnedByUser(id: java.util.UUID, id2: java.util.UUID): Boolean
}