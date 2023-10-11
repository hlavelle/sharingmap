package com.sharingmap.services

import com.sharingmap.entities.ItemEntity
import java.util.*

interface ItemService {
    fun getItemById(id: UUID): ItemEntity
    fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): List<ItemEntity>
    fun createItem(item: ItemEntity): ItemEntity?
    fun deleteItem(id: UUID): Boolean
    fun updateItem(item: ItemEntity)
    fun getAllItemsByUserId(userId: UUID): List<ItemEntity>
}