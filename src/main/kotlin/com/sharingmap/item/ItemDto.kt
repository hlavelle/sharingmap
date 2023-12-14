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
    val locationId: Long?,
    val imagesId: List<UUID>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val categoryId: Long?,
    val subcategoryId: Long?,
    val cityId: Long?,
    val userId: UUID?)

data class ItemCreateDto(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Text is required")
    val text: String,
    @field:NotNull(message = "CategoryId is required")
    val categoryId: Long,
    @field:NotNull(message = "SubcategoryId is required")
    val subcategoryId: Long,
    @field:NotNull(message = "CityId is required")
    val cityId: Long,
    @field:NotNull(message = "LocationId is required")
    val locationId: Long
    )

data class ItemUpdateDto(
    val name: String?,
    val text: String?,
    val categoryId: Long?,
    val cityId: Long?,
    val locationId: Long?
)