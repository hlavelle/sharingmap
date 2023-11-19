package com.sharingmap.services

import java.util.*

interface UserImageService {
    fun getPresignedUrlAndReplace(objectId: UUID): String?

    fun setImageUploaded(imageId: UUID): Boolean
}