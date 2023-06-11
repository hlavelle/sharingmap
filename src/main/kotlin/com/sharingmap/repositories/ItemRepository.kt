package com.sharingmap.repositories

import com.sharingmap.entities.ItemEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository <ItemEntity, Long> {
    fun findAllByUserId(userId: Long, sort: Sort): List<ItemEntity>
    fun findAllByCategoryIdAndSubcategoryIdAndCityId(categoryId: Long, subcategoryId: Long, cityId: Long, pageable: PageRequest): List<ItemEntity>
    abstract fun findAllByCategoryIdAndSubcategoryId(categoryId: Long, subcategoryId: Long, pageable: PageRequest): List<ItemEntity>
    abstract fun findAllByCategoryIdAndCityId(categoryId: Long, cityId: Long, pageable: PageRequest): List<ItemEntity>
    abstract fun findAllByCategoryId(categoryId: Long, pageable: PageRequest): List<ItemEntity>
    abstract fun findAllBySubcategoryIdAndCityId(subcategoryId: Long, cityId: Long, pageable: PageRequest): List<ItemEntity>
    abstract fun findAllBySubcategoryId(subcategoryId: Long, pageable: PageRequest): List<ItemEntity>
    abstract fun findAllByCityId(cityId: Long, pageable: PageRequest): List<ItemEntity>
}