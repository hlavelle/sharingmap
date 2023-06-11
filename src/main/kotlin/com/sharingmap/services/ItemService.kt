package com.sharingmap.services

import com.sharingmap.entities.ItemEntity

interface ItemService {
    abstract fun getItemById(id: Long): ItemEntity
    abstract fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): List<ItemEntity>
    abstract fun createItem(userId: Long, categoryId: Long, subcategoryId: Long, cityId: Long, item: ItemEntity)
    abstract fun deleteItem(id: Long): Boolean
    abstract fun updateItem(id: Long, item: ItemEntity)
    abstract fun getAllItemsByUserId(userId: Long): List<ItemEntity>
}