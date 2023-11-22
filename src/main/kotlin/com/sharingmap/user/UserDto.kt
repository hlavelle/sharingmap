package com.sharingmap.user

import java.util.*

class UserUpdateDto(
    var id: UUID,
    var username: String,
    var bio: String
)
class UserInfoDto (
    var username: String,
    var id: UUID,
    var bio: String,
    var email: String,
    var hasImage: Boolean
)
class UserDto(
    var username: String? = null,
    var bio: String? = null
)