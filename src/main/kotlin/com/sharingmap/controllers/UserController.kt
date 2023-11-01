package com.sharingmap.controllers

import com.sharingmap.entities.UserEntity
import com.sharingmap.services.UserService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: UUID): UserEntity {
        return userService.getUserById(id)
    }

    @GetMapping("/users/all")
    fun getAllUsers(): List<UserEntity> {
        return userService.getAllUsers()
    }

    @DeleteMapping("/users/delete/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }

    @PutMapping("/users/update/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody user: UserEntity) {
        userService.updateUser(id, user)
    }
}