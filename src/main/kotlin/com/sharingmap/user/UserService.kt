package com.sharingmap.user

import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService : UserDetailsService {
    fun getUserById(id: UUID): UserEntity
    fun getAllUsers(): List<UserEntity>
    fun deleteUser(id: UUID)
    fun updateUser(id: UUID, userDto: UserDto)
    fun changePassword(id: UUID, password: String)
}