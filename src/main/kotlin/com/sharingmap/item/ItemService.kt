package com.sharingmap.item

import org.springframework.data.domain.Page
import java.util.*

interface ItemService {
    fun getItemById(id: UUID): ItemEntity
    fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): Page<ItemEntity>
    fun createItem(item: ItemEntity): ItemEntity?
    fun deleteItem(id: UUID): Boolean
    fun updateItem(item: ItemEntity)
    fun getAllItemsByUserId(userId: UUID, page: Int, size: Int): Page<ItemEntity>
}