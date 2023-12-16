package com.sharingmap.user

import java.util.*

data class UserInfoDto (
    var username: String,
    var id: UUID,
    var bio: String,
    var email: String,
    var hasImage: Boolean
)
data class UserDto(
    var username: String? = null,
    var bio: String? = null
)

data class GetOtherUserDto(
    val username: String,
    val bio: String,
    val hasImage: Boolean
)