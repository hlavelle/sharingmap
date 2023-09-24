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

    @GetMapping("/users")
    fun getAllUsers(): List<UserEntity> {
        return userService.getAllUsers()
    }

    @PostMapping("/users")
    fun createUser(@RequestBody user: UserEntity) {
        userService.createUser(user)
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody user: UserEntity) {
        userService.updateUser(id, user)
    }
}