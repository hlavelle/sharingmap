package com.sharingmap.services

import com.sharingmap.entities.UserEntity

interface UserService {
    abstract fun getUserById(id: Long): UserEntity
    abstract fun getAllUsers(): List<UserEntity>
    abstract fun createUser(user: UserEntity)
    abstract fun deleteUser(id: Long)
    abstract fun updateUser(id: Long, user: UserEntity)
}