package com.sharingmap.item

import com.sharingmap.image.ImageService
import com.sharingmap.image.ItemImageEntity
import com.sharingmap.user.UserService
import org.springframework.stereotype.Component

@Component
class ItemDtoMapper(
    private val itemImageService: ImageService<ItemImageEntity>,
    private val userService: UserService
) {
    fun toDto(itemEntity: ItemEntity): ItemDto {
        return ItemDto(
            id = itemEntity.id,
            name = itemEntity.name,
            text = itemEntity.text,
            locationsId = itemEntity.locations.mapNotNull { it.id },
            imagesId = itemEntity.images?.map { it.id } ?: listOf(),
            createdAt = itemEntity.createdAt,
            updatedAt = itemEntity.updatedAt,
            categoriesId = itemEntity.categories.mapNotNull { it.id },
            subcategoryId = itemEntity.subcategory?.id,
            cityId = itemEntity.city.id,
            userId = itemEntity.user?.id,
            user = itemEntity.user?.let { user -> userService.toItemUserDto(user) },
            itemPhoto = itemEntity.images?.map { itemImageService.toImageDto(it) } ?: listOf(),
            addressInfo = itemEntity.address?.description
        )
    }
}
