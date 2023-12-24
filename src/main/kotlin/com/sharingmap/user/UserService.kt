package com.sharingmap.user

import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface UserService : UserDetailsService {
    fun getUserById(id: UUID): UserEntity
    fun getAllUsers(): List<UserEntity>
    fun deleteUser(userId: UUID)
    fun makeUserDisabled(userId: UUID)
    fun updateUser(userId: UUID, userDto: UserDto)
    fun changePassword(id: UUID, password: String)
}