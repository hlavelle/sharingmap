package com.sharingmap.services

import com.sharingmap.entities.ItemEntity

interface ItemService {
    abstract fun getItemById(id: Long): ItemEntity
    abstract fun getAllItems(categoryId: Long, cityId: Long): List<ItemEntity>
    abstract fun createItem(userId: Long, categoryId: Long, cityId: Long, item: ItemEntity)
    abstract fun deleteItem(id: Long)
    abstract fun updateItem(id: Long, item: ItemEntity)
    abstract fun getAllItemsByUserId(userId: Long): List<ItemEntity>
}