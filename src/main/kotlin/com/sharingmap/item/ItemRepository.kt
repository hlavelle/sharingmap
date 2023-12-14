package com.sharingmap.item

import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
interface ItemRepository : JpaRepository <ItemEntity, UUID> {
    fun findAllByUserId(userId: UUID, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoryIdAndSubcategoryIdAndCityId(categoryId: Long, subcategoryId: Long, cityId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoryIdAndSubcategoryId(categoryId: Long, subcategoryId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoryIdAndCityId(categoryId: Long, cityId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoryId(categoryId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryIdAndCityId(subcategoryId: Long, cityId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryId(subcategoryId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCityId(cityId: Long, pageable: Pageable): Page<ItemEntity>
}