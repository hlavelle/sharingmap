package com.sharingmap.item

import com.sharingmap.image.ItemImageEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
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
    val userId: UUID?)

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
    val locationsId: List<Long>?
)