package com.sharingmap.controllers

import com.sharingmap.entities.UserEntity
import com.sharingmap.services.UserService
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/{id}") //TODO сделать возвращение юзеру всей инфы о себе и частичной о других юзерах.
    fun getUserById(@PathVariable id: UUID, principal: Principal): UserEntity {
        return userService.getUserById(id)
    }

    @GetMapping("/users/all")
    fun getAllUsers(): List<UserEntity> {
        return userService.getAllUsers()
    }

    @DeleteMapping("/users/{id}/delete") //TODO сделать проверку юзера
    fun deleteUser(@PathVariable id: UUID, principal: Principal) {
        userService.deleteUser(id)
    }

    @PutMapping("/users/{id}/update") //TODO сделать проверку юзера
    fun updateUser(@PathVariable id: UUID, @RequestBody user: UserEntity, principal: Principal) {
        userService.updateUser(id, user)
    }
}