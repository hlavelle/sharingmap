package com.sharingmap.item

import org.springframework.data.domain.Page
import java.util.*

interface ItemService {
    fun getItemById(id: UUID): ItemEntity
    fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): Page<ItemEntity>
    fun createItem(userId: UUID, item: ItemCreateDto): ItemEntity?

    fun adminDeleteItem(itemId: UUID)
    fun deleteItem(userId: UUID, itemId: UUID)

    fun adminUpdateItem(item: ItemUpdateDto)
    fun updateItem(userId: UUID, item: ItemUpdateDto)
    fun getAllItemsByUserId(userId: UUID, page: Int, size: Int): Page<ItemEntity>
}