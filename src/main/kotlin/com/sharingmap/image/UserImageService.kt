package com.sharingmap.image

import java.util.*

interface UserImageService {
    fun getPresignedUrlAndReplace(objectId: UUID): String?

    fun setImageUploaded(imageId: UUID): Boolean
}