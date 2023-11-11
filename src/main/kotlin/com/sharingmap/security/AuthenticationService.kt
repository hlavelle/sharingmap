package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import com.sharingmap.security.registration.RegistrationRequest

interface AuthenticationService {
    fun createUser(request: RegistrationRequest): UserEntity

    fun confirmToken(token: String, tokenId: String): String
}