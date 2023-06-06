package com.sharingmap.repositories

import com.sharingmap.entities.ItemEntity
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository <ItemEntity, Long> {
    fun findAllByUserId(userId: Long, sort: Sort): List<ItemEntity>
    fun findAllByCategoryIdAndCityId(categoryId: Long, cityId: Long, sort: Sort): List<ItemEntity>
    fun findAllByCategoryId(categoryId: Long, sort: Sort): List<ItemEntity>
    fun findAllByCityId(cityId: Long, sort: Sort): List<ItemEntity>
}