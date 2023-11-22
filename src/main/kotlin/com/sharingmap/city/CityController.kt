package com.sharingmap.city

import org.springframework.web.bind.annotation.*

@RestController
class CityController(private val cityService: CityService) {

    @GetMapping("/cities/{id}")
    fun getCityById(@PathVariable id: Long): CityEntity {
        return cityService.getCityById(id)
    }

    @GetMapping("/cities/all")
    fun getAllCities(): List<CityEntity> {
        return cityService.getAllCities()
    }

    @PostMapping("/cities/create")
    fun createCity(@RequestBody city: CityEntity) {
        cityService.createCity(city)
    }

    @DeleteMapping("/cities/delete/{id}")
    fun deleteCity(@PathVariable id: Long) {
        cityService.deleteCity(id)
    }

    @PutMapping("/cities/update/{id}")
    fun updateCity(@PathVariable id: Long, @RequestBody city: CityEntity) {
        cityService.updateCity(id, city)
    }
}