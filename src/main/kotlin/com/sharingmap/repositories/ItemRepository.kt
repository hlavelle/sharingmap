package com.sharingmap.repositories

import com.sharingmap.entities.ItemEntity
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository <ItemEntity, Long> {
    abstract fun findAllByUserId(userId: Long, sort: Sort): List<ItemEntity>
    abstract fun findAllByCategoryIdAndCityId(categoryId: Long, cityId: Long, sort: Sort): List<ItemEntity>
    abstract fun findAllByCategoryId(categoryId: Long, sort: Sort): List<ItemEntity>
    abstract fun findAllByCityId(cityId: Long, sort: Sort): List<ItemEntity>
}