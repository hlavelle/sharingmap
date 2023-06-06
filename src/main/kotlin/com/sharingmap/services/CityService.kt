package com.sharingmap.services

import com.sharingmap.entities.CityEntity

interface CityService {
    abstract fun getCityById(id: Long): CityEntity
    abstract fun getAllCities(): List<CityEntity>
    abstract fun createCity(city: CityEntity)
    abstract fun deleteCity(id: Long)
    abstract fun updateCity(id: Long, city: CityEntity)
}