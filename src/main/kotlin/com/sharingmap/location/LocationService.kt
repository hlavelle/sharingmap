package com.sharingmap.location


interface LocationService {
    fun getLocationById(id: Long): LocationEntity
    fun getAllLocations(): List<LocationEntity>
    fun createLocation(location: LocationEntity)
    fun deleteLocation(id: Long)
    fun updateLocation(id: Long, location: LocationEntity)
}