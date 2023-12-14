package com.sharingmap.city

import org.springframework.stereotype.Service

@Service
class CityServiceImpl(private val cityRepository: CityRepository) : CityService {

    override fun getCityById(id: Long): CityEntity {
        return cityRepository.findById(id).orElseThrow { CityNotFoundException("City not found with ID: $id") }
    }

    override fun getAllCities(): List<CityEntity> = cityRepository.findAll().toList()

    override fun createCity(city: CityEntity) {
        cityRepository.save(city)
    }

    override fun deleteCity(id: Long) {
        cityRepository.deleteById(id)
    }

    override fun updateCity(id: Long, city: CityEntity) {
        val newCity = cityRepository.findById(id).get()
        newCity.name = city.name
        cityRepository.save(newCity)
    }

}