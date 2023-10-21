package com.sharingmap.services

import com.sharingmap.entities.UserEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService : UserDetailsService {
    abstract fun getUserById(id: UUID): UserEntity
    abstract fun getAllUsers(): List<UserEntity>
    abstract fun deleteUser(id: UUID)
    abstract fun updateUser(id: UUID, user: UserEntity)

    fun retrieveFromCache(email: String): UserDetails
}