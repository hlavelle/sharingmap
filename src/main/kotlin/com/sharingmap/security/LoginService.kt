package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import org.springframework.security.core.userdetails.UserDetails

interface LoginService {

    fun login(email: String, password: String): UserEntity?
}