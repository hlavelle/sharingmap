package com.sharingmap.adresses

import com.sharingmap.city.CityRepository
import com.sharingmap.location.LocationRepository
import com.sharingmap.user.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID
@Service
@Transactional
class AddressServiceImpl(
    private val addressRepository: AddressRepository,
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    private val cityRepository: CityRepository
) : AddressService {

    @Transactional(readOnly = true)
    override fun findAllByUserId(userId: UUID): List<AddressResponseDto> {
        val addresses = addressRepository.findAllByUserId(userId)
        return addresses.map { it.toResponseDto() }
    }

    @Transactional(readOnly = true)
    override fun findById(id: UUID): AddressEntity {
        val address = addressRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Address not found with id: $id") }
        return address
    }

    override fun createAddress(userId: UUID, createAddressDto: CreateAddressDto): AddressResponseDto {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found with id: $userId") }

        val city = cityRepository.findById(createAddressDto.cityId)
            .orElseThrow { EntityNotFoundException("City not found with id: ${createAddressDto.cityId}") }

        val locations = locationRepository.findAllById(createAddressDto.locationIds)
        if (locations.size != createAddressDto.locationIds.size) {
            val foundIds = locations.map { it.id }.toSet()
            val missingIds = createAddressDto.locationIds - foundIds
            throw EntityNotFoundException("Locations not found with ids: $missingIds")
        }

        val address = AddressEntity(
            name = createAddressDto.name,
            description = createAddressDto.description,
            user = user,
            locations = locations.toSet(),
            city = city
        )

        val savedAddress = addressRepository.save(address)
        return savedAddress.toResponseDto()
    }

    override fun updateAddress(id: UUID, updateAddressDto: UpdateAddressDto): AddressResponseDto {
        val address = addressRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Address not found with id: $id") }

        // Update fields if provided
        updateAddressDto.name?.let { address.name = it }
        updateAddressDto.description?.let { address.description = it }

        updateAddressDto.cityId?.let { cityId ->
            val city = cityRepository.findById(cityId)
                .orElseThrow { EntityNotFoundException("City not found with id: $cityId") }
            address.city = city
        }

        updateAddressDto.locationIds?.let { locationIds ->
            val locations = locationRepository.findAllById(locationIds)
            if (locations.size != locationIds.size) {
                val foundIds = locations.map { it.id }.toSet()
                val missingIds = locationIds - foundIds
                throw EntityNotFoundException("Locations not found with ids: $missingIds")
            }
            address.locations = locations.toSet()
        }

        address.updatedAt = LocalDateTime.now()
        val savedAddress = addressRepository.save(address)
        return savedAddress.toResponseDto()
    }

    override fun deleteAddress(id: UUID) {
        if (!addressRepository.existsById(id)) {
            throw EntityNotFoundException("Address not found with id: $id")
        }
        addressRepository.deleteById(id)
    }

    private fun AddressEntity.toResponseDto(): AddressResponseDto {
        return AddressResponseDto(
            id = this.id,
            name = this.name,
            description = this.description,
            locationIds = this.locations.map { it.id }.toSet(),
            cityId = this.city.id,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            user_id = this.user.id
        )
    }

    override fun isAddressOwnedByUser(id: UUID, id2: UUID): Boolean {
        return addressRepository.existsByIdAndUserId(id, id2)
    }
}