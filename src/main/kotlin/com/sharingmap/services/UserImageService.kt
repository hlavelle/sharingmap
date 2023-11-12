package com.sharingmap.services

import java.util.*

interface UserImageService {
    fun getPresignedUrls(objectId: UUID, count: Int): List<String>

    fun setImageUploaded(imageId: UUID): Boolean
}