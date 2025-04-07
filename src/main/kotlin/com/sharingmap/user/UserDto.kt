package com.sharingmap.user

import com.sharingmap.image.ImageDto
import java.util.*

data class UserInfoDto (
    var id: UUID,
    var username: String,
    var bio: String,
    var hasImage: Boolean,
    val photo: ImageDto?
)
data class UserDto(
    var username: String? = null,
    var bio: String? = null
)

data class ItemUserDto(
    val id: UUID,
    val username: String,
    val photo: ImageDto?
)
