package com.sharingmap.location

import org.springframework.web.bind.annotation.*

@RestController
class LocationController(private val locationService: LocationService) {
    @GetMapping("/locations/{id}")
    fun getLocationById(@PathVariable id: Long): LocationEntity {
        return locationService.getLocationById(id)
    }

    @GetMapping("/locations/all")
    fun getAllLocations(): List<LocationEntity> {
        return locationService.getAllLocations()
    }

    @PostMapping("/locations/create")
    fun createLocation(@RequestBody location: LocationEntity) {
        locationService.createLocation(location)
    }

    @DeleteMapping("/locations/delete/{id}")
    fun deleteLocation(@PathVariable id: Long) {
        locationService.deleteLocation(id)
    }

    @PutMapping("/locations/update/{id}")
    fun updateLocation(@PathVariable id: Long, @RequestBody location: LocationEntity) {
        locationService.updateLocation(id, location)
    }
}