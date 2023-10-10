package com.sharingmap.security

import com.sharingmap.entities.UserEntity

interface AuthenticationService {
    fun createUser(user: UserEntity): String

    fun confirmToken(token: String): String
}