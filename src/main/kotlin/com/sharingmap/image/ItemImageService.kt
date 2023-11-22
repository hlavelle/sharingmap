package com.sharingmap.image

import com.sharingmap.image.ItemImageEntity
import java.util.*

interface ItemImageService {
    fun getPresignedUrls(objectId: UUID, count: Int): List<String>

    fun getItemImages(objectId: UUID): List<ItemImageEntity>

    fun setImageUploaded(imageId: UUID): Boolean
}