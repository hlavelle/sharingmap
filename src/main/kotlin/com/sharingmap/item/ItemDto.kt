package com.sharingmap.item

import com.sharingmap.image.ItemImageEntity
import java.time.LocalDateTime
import java.util.*

data class ItemDto(
    val id: UUID?,
    val name: String?,
    val text: String?,
    val locationId: Long?,
    val imagesId: List<UUID>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val categoryId: Long?,
    val subcategoryId: Long?,
    val cityId: Long?,
    val userId: UUID?)