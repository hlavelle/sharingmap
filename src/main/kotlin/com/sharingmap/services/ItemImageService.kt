package com.sharingmap.services

import com.sharingmap.entities.ItemImageEntity
import java.util.*

interface ItemImageService {
    fun getPresignedUrls(objectId: UUID, count: Int): List<String>

    fun getItemImages(objectId: UUID): List<ItemImageEntity>

    fun setImageUploaded(imageId: UUID): Boolean
}