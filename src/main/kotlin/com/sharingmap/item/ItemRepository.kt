package com.sharingmap.item

import com.sharingmap.user.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
interface ItemRepository : JpaRepository <ItemEntity, UUID> {
    fun findAllByUserId(userId: UUID, pageable: Pageable): Page<ItemEntity>
    fun findAllByUser(user: UserEntity): List<ItemEntity>
    fun findAllByCategoriesIdAndSubcategoryIdAndCityIdAndUserEnabled(categoryId: Long, subcategoryId: Long, cityId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndSubcategoryIdAndUserEnabled(categoryId: Long, subcategoryId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndCityIdAndUserEnabled(categoryId: Long, cityId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndUserEnabled(categoryId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryIdAndCityIdAndUserEnabled(subcategoryId: Long, cityId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryIdAndUserEnabled(subcategoryId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCityIdAndUserEnabled(cityId: Long, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByUserEnabled(enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun deleteByUser(user: UserEntity)
}