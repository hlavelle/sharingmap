package com.sharingmap.services

import com.sharingmap.entities.CategoryEntity
import com.sharingmap.entities.CityEntity
import com.sharingmap.repositories.CategoryRepository
import com.sharingmap.repositories.CityRepository
import org.springframework.stereotype.Service

@Service
class CityServiceImpl(private val cityRepository: CityRepository) : CityService {

    override fun getCityById(id: Long): CityEntity = cityRepository.findById(id).get()

    override fun getAllCities(): List<CityEntity> = cityRepository.findAll().toList()

    override fun createCity(city: CityEntity) {
        cityRepository.save(city)
    }

    override fun deleteCity(id: Long) {
        cityRepository.deleteById(id)
    }

    override fun updateCity(id: Long, city: CityEntity) {
        var newCity = cityRepository.findById(id).get()
        newCity.name = city.name
        cityRepository.save(newCity)
    }

}