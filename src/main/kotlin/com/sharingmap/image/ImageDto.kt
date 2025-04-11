package com.sharingmap.image

import java.util.*

data class ImageDto(
    val imageId: UUID,
    val objectId: UUID,
    val resolution: String,
    val url: String
    )