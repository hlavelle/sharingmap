package com.sharingmap.services

import com.sharingmap.entities.UserEntity
import java.util.*

interface UserService {
    abstract fun getUserById(id: UUID): UserEntity
    abstract fun getAllUsers(): List<UserEntity>
    abstract fun createUser(user: UserEntity)
    abstract fun deleteUser(id: UUID)
    abstract fun updateUser(id: UUID, user: UserEntity)
}