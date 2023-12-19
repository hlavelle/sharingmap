package com.sharingmap.location

import com.sharingmap.city.CityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException

@RestController
class LocationController(private val locationService: LocationService) {
    @GetMapping("/locations/{id}")
    fun getLocationById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val location = locationService.getLocationById(id)
            ResponseEntity.ok(location)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Location not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @GetMapping("/locations/{cityId}/all")
    fun getAllLocationsByCity(@PathVariable cityId: Long): ResponseEntity<Any> {
        return try {
            val locationInfoDtos = locationService.getAllLocationsByCity(cityId).map { LocationInfoDto(it) }
            ResponseEntity.ok(locationInfoDtos)
        } catch (ex: CityNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @PostMapping("/locations/create")
    fun createLocation(@RequestBody location: LocationCreateDto): ResponseEntity<Any> {
        return try {
            locationService.createLocation(location)
            ResponseEntity.status(HttpStatus.CREATED).body("Location created successfully")
        } catch (ex: CityNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @DeleteMapping("/locations/delete/{id}")
    fun deleteLocation(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            locationService.deleteLocation(id)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Location not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/locations/update/{id}")
    fun updateLocation(@PathVariable id: Long, @RequestBody location: LocationUpdateDto): ResponseEntity<Any> {
        return try {
            locationService.updateLocation(id, location)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Location not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }
}