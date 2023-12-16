package com.sharingmap.item

import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
interface ItemRepository : JpaRepository <ItemEntity, UUID> {
    fun findAllByUserId(userId: UUID, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndSubcategoryIdAndCityId(categoryId: Long, subcategoryId: Long, cityId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndSubcategoryId(categoryId: Long, subcategoryId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndCityId(categoryId: Long, cityId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesId(categoryId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryIdAndCityId(subcategoryId: Long, cityId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryId(subcategoryId: Long, pageable: Pageable): Page<ItemEntity>
    fun findAllByCityId(cityId: Long, pageable: Pageable): Page<ItemEntity>
}