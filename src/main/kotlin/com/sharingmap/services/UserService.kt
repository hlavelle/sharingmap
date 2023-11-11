package com.sharingmap.services

import com.sharingmap.entities.UserEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService : UserDetailsService {
    fun getUserById(id: UUID): UserEntity
    fun getAllUsers(): List<UserEntity>
    fun deleteUser(id: UUID)
    fun updateUser(id: UUID, user: UserEntity)
    fun changePassword(id: UUID, password: String)
}