package com.sharingmap.location


interface LocationService {
    fun getLocationById(id: Long): LocationEntity
    fun getAllLocationsByCity(cityId: Long): List<LocationEntity>
    fun createLocation(location: LocationCreateDto)
    fun deleteLocation(id: Long)
    fun updateLocation(id: Long, location: LocationUpdateDto)
}