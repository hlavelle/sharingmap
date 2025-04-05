package com.sharingmap.image

import org.springframework.stereotype.Service
import java.util.*
import com.sharingmap.city.CityEntity
import com.sharingmap.item.ItemEntity
import com.sharingmap.item.State

@Service
class ImageServiceImpl(private val repository: ItemImageRepository) : ImageService<ItemImageEntity>() {
    override fun saveImageAndGetId(objectId: UUID): UUID {
        val newImage = ItemImageEntity(entity = ItemEntity(id = objectId, state = State.ACTIVE, name = "", city = CityEntity()))
        return repository.save(newImage).id
    }
    override fun getImages(objectId: UUID): List<ItemImageEntity> {
        return repository.findAllByEntityId(objectId)
    }
    override fun getImagePath(image: ItemImageEntity): String {
        return getImagePath(image.entity.id, image.id)
    }
    override fun getImagePath(itemId: UUID, imageId: UUID): String {
        return "${itemId}/${imageId}"
    }
    override fun toImageDto(image: ItemImageEntity): ImageDto {
        return ImageDto(
            objectId = image.entity.id,
            imageId = image.id,
            resolution = image.resolution.toString(),
            url = generateImageUrl(image)
        )
    }
}
