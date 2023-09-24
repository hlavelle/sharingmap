package com.sharingmap.services

import com.sharingmap.entities.UserEntity

interface AuthenticationService {
    fun createUser(user: UserEntity): Boolean
}