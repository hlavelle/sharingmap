package com.sharingmap.location

import org.springframework.stereotype.Service

@Service
class LocationServiceImpl(private val locationRepository: LocationRepository) : LocationService {
    override fun getLocationById(id: Long): LocationEntity = locationRepository.findById(id).get()
    override fun getAllLocations(): List<LocationEntity> = locationRepository.findAll().toList() //TODO поиск по городам и сортировка по алфавиту
    override fun createLocation(location: LocationEntity) {
        locationRepository.save(location)
    }
    override fun deleteLocation(id: Long) {
        locationRepository.deleteById(id)
    }
    override fun updateLocation(id: Long, location: LocationEntity) {
        var newLocation = locationRepository.findById(id).get()
        newLocation.name = location.name
        newLocation.type = location.type
        locationRepository.save(newLocation)
    }
}