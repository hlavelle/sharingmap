package com.sharingmap.controllers

import com.sharingmap.entities.UserEntity
import com.sharingmap.services.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Long): UserEntity {
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
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserEntity) {
        userService.updateUser(id, user)
    }
}