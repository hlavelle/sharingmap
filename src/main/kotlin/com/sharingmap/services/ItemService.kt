package com.sharingmap.services

import com.sharingmap.entities.ItemEntity

interface ItemService {
    fun getItemById(id: Long): ItemEntity
    fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): List<ItemEntity>
    fun createItem(userId: Long, categoryId: Long, subcategoryId: Long, cityId: Long, item: ItemEntity)
    fun deleteItem(id: Long): Boolean
    fun updateItem(id: Long, item: ItemEntity)
    fun getAllItemsByUserId(userId: Long): List<ItemEntity>
}