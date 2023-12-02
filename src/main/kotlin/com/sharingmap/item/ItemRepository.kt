package com.sharingmap.item

import com.sharingmap.item.ItemEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ItemRepository : JpaRepository <ItemEntity, UUID> {
    fun findAllByUserId(userId: UUID, sort: Sort): List<ItemEntity>
    fun findAllByCategoryIdAndSubcategoryIdAndCityId(categoryId: Long, subcategoryId: Long, cityId: Long, pageable: PageRequest): List<ItemEntity>
    fun findAllByCategoryIdAndSubcategoryId(categoryId: Long, subcategoryId: Long, pageable: PageRequest): List<ItemEntity>
    fun findAllByCategoryIdAndCityId(categoryId: Long, cityId: Long, pageable: PageRequest): List<ItemEntity>
    fun findAllByCategoryId(categoryId: Long, pageable: PageRequest): List<ItemEntity>
    fun findAllBySubcategoryIdAndCityId(subcategoryId: Long, cityId: Long, pageable: PageRequest): List<ItemEntity>
    fun findAllBySubcategoryId(subcategoryId: Long, pageable: PageRequest): List<ItemEntity>
    fun findAllByCityId(cityId: Long, pageable: PageRequest): List<ItemEntity>
}