package com.sharingmap.security.login

import com.sharingmap.entities.UserEntity
import org.springframework.security.core.userdetails.UserDetails

interface LoginService {

    fun login(email: String, password: String): UserEntity?
}