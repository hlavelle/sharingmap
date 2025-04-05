package com.sharingmap.image

import aws.smithy.kotlin.runtime.net.Url
import org.springframework.stereotype.Service
import java.util.*
import com.sharingmap.user.Role
import com.sharingmap.user.UserEntity

@Service
class UserImageServiceImpl(private val repository: UserImageRepository) : ImageService<UserImageEntity>() {
    override fun saveImageAndGetId(objectId: UUID): UUID {
        val newImage = UserImageEntity(
            id = objectId,
            entity = UserEntity(id = objectId, username = "", password = "", email = "", role = Role.ROLE_USER))
        return repository.save(newImage).id
    }

    override fun getImages(objectId: UUID): List<UserImageEntity> {
        return repository.findAllByEntityId(objectId)
    }
    override fun getImagePath(image: UserImageEntity): String {
        return getImagePath(image.entity.id, image.entity.id)
    }
    override fun getImagePath(itemId: UUID, imageId: UUID): String {
        return "${itemId}/${itemId}"
    }

    override fun toImageDto(image: UserImageEntity): ImageDto {
        return ImageDto(
            objectId = image.entity.id,
            imageId = image.id,
            resolution = image.resolution.toString(),
            url = generateImageUrl(image)
        )
    }
}
