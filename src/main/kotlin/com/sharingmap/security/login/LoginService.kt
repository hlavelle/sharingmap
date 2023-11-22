package com.sharingmap.security.login

import com.sharingmap.user.UserEntity

interface LoginService {

    fun login(email: String, password: String): UserEntity
}