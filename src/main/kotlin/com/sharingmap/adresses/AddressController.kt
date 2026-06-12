package com.sharingmap.adresses

import com.sharingmap.city.CityNotFoundException
import com.sharingmap.user.UserEntity
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException
import java.util.UUID

@RestController
@Validated
class AddressController(
    private val addressService: AddressService
) {
    @GetMapping("/address/all")
    fun getAllAddressesByUser(): ResponseEntity<List<AddressResponseDto>> {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            if (!authentication.isAuthenticated) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            val user = authentication.principal as UserEntity

            val addresses = addressService.findAllByUserId(user.id)
            ResponseEntity.ok(addresses)
        } catch (ex: Exception) {
            print(ex.toString())
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/address/add")
    fun addAddress(
        @Valid @RequestBody createAddressDto: CreateAddressDto
    ): ResponseEntity<AddressResponseDto> {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            if (!authentication.isAuthenticated) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            val user = authentication.principal as UserEntity

            val address = addressService.createAddress(user.id, createAddressDto)
            ResponseEntity.status(HttpStatus.OK).body(address)

        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("/address/delete/{id}")
    fun deleteAddress(@PathVariable id: UUID): ResponseEntity<Void> {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            if (!authentication.isAuthenticated) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            val user = authentication.principal as UserEntity

            if (!addressService.isAddressOwnedByUser(id, user.id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            }

            addressService.deleteAddress(id)
            ResponseEntity.noContent().build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/address/update/{id}")
    fun updateAddress(
        @PathVariable id: UUID,
        @Valid @RequestBody updateAddressDto: UpdateAddressDto
    ): ResponseEntity<AddressResponseDto> {
        return try {
            val authentication = SecurityContextHolder.getContext().authentication
            if (!authentication.isAuthenticated) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            val user = authentication.principal as UserEntity

            if (!addressService.isAddressOwnedByUser(id, user.id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            }

            val address = addressService.updateAddress(id, updateAddressDto)

            ResponseEntity.ok(address)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}