package com.sharingmap.services

import com.sharingmap.entities.UserEntity
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    abstract fun getUserById(id: Long): UserEntity
    abstract fun getAllUsers(): List<UserEntity>
    abstract fun deleteUser(id: Long)
    abstract fun updateUser(id: Long, user: UserEntity)
}