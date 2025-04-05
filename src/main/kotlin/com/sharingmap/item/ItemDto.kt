package com.sharingmap.item

import com.sharingmap.image.ImageDto
import com.sharingmap.user.ItemUserDto
import java.time.LocalDateTime
import java.util.*

data class ItemDto(
    val id: UUID?,
    val name: String?,
    val text: String?,
    val locationsId: List<Long>?,
    val imagesId: List<UUID>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val categoriesId: List<Long>?,
    val subcategoryId: Long?,
    val cityId: Long?,
    val userId: UUID?,
    val user: ItemUserDto?,
    val itemPhoto: List<ImageDto>?
    )

data class ItemCreateDto(
    val name: String,
    val text: String,
    val categoriesId: List<Long>,
    val subcategoryId: Long,
    val cityId: Long,
    val locationsId: List<Long>
    )

data class ItemUpdateDto(
    val name: String?,
    val text: String?,
    val categoriesId: List<Long>?,
    val cityId: Long?,
    val locationsId: List<Long>?,
    val id: UUID
)