package com.sharingmap.city

interface CityService {
    fun getCityById(id: Long): CityEntity
    fun getAllCities(): List<CityEntity>
    fun createCity(city: CityEntity)
    fun deleteCity(id: Long)
    fun updateCity(id: Long, city: CityEntity)
}