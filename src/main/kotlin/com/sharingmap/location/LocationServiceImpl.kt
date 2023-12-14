package com.sharingmap.location

import com.sharingmap.city.CityNotFoundException
import com.sharingmap.city.CityRepository
import com.sharingmap.city.CityService
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class LocationServiceImpl(private val locationRepository: LocationRepository,
                          private val cityService: CityService,
                          private val cityRepository: CityRepository
) : LocationService {
    override fun getLocationById(id: Long): LocationEntity {
        return locationRepository.findById(id).orElseThrow { NoSuchElementException("Location not found with ID: $id") }
    }
    override fun getAllLocationsByCity(cityId: Long): List<LocationEntity> {
        val city = cityRepository.findById(cityId)
            .orElseThrow { CityNotFoundException("User not found with ID: $cityId") }
        return locationRepository.findAllByCityOrderByNameAsc(city).toList()
    }
    override fun createLocation(location: LocationCreateDto) { //TODO validate that name is unique for city
        val city = cityService.getCityById(location.cityId)

        val locationEntity = LocationEntity(name = location.name, type = location.type, city = city)
        locationRepository.save(locationEntity)
    }
    override fun deleteLocation(id: Long) {
        locationRepository.findById(id)
            .orElseThrow { NoSuchElementException("Location not found with ID: $id") }
        locationRepository.deleteById(id)
    }
    override fun updateLocation(id: Long, location: LocationUpdateDto) { //TODO validate that name is unique for city
        val newLocation = locationRepository.findById(id)
            .orElseThrow { NoSuchElementException("Location not found with ID: $id") }

        if (location.name != null) newLocation.name = location.name
        locationRepository.save(newLocation)
    }
}