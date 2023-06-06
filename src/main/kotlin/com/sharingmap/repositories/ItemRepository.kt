package com.sharingmap.repositories

import com.sharingmap.entities.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository <ItemEntity, Long> {
    abstract fun findAllByUserId(userId: Long): List<ItemEntity>
    abstract fun findAllByCategoryIdAndCityId(categoryId: Long, cityId: Long): List<ItemEntity>
    abstract fun findAllByCategoryId(categoryId: Long): List<ItemEntity>
    abstract fun findAllByCityId(cityId: Long): List<ItemEntity>
}