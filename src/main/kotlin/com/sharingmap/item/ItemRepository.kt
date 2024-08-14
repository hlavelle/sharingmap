package com.sharingmap.item

import com.sharingmap.user.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
interface ItemRepository : JpaRepository <ItemEntity, UUID> {
    fun findAllByUserIdAndState(userId: UUID, state: State, pageable: Pageable): Page<ItemEntity>
    fun findAllByUser(user: UserEntity): List<ItemEntity>
    fun findAllByCategoriesIdAndSubcategoryIdAndCityIdAndStateAndUserEnabled(categoryId: Long, subcategoryId: Long, cityId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndSubcategoryIdAndStateAndUserEnabled(categoryId: Long, subcategoryId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndCityIdAndStateAndUserEnabled(categoryId: Long, cityId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCategoriesIdAndStateAndUserEnabled(categoryId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryIdAndCityIdAndStateAndUserEnabled(subcategoryId: Long, cityId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllBySubcategoryIdAndStateAndUserEnabled(subcategoryId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByCityIdAndStateAndUserEnabled(cityId: Long, state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun findAllByStateAndUserEnabled(state: State, enabled: Boolean, pageable: Pageable): Page<ItemEntity>
    fun deleteByUser(user: UserEntity)
}