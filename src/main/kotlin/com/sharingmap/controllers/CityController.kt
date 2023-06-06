package com.sharingmap.controllers

import com.sharingmap.entities.CategoryEntity
import com.sharingmap.entities.CityEntity
import com.sharingmap.services.CategoryService
import com.sharingmap.services.CityService
import org.springframework.web.bind.annotation.*

@RestController
class CityController(private val cityService: CityService) {

    @GetMapping("/cities/{id}")
    fun getCityById(@PathVariable id: Long): CityEntity {
        return cityService.getCityById(id)
    }

    @GetMapping("/cities")
    fun getAllCities(): List<CityEntity> {
        return cityService.getAllCities()
    }

    @PostMapping("/cities")
    fun createCity(@RequestBody city: CityEntity) {
        cityService.createCity(city)
    }

    @DeleteMapping("/cities/{id}")
    fun deleteCity(@PathVariable id: Long) {
        cityService.deleteCity(id)
    }

    @PutMapping("/cities/{id}")
    fun updateCity(@PathVariable id: Long, @RequestBody city: CityEntity) {
        cityService.updateCity(id, city)
    }
}