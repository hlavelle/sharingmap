package com.sharingmap.controllers

import com.sharingmap.dto.UserDto
import com.sharingmap.entities.UserEntity
import com.sharingmap.services.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/id")
    fun getUserInfoById(@RequestParam id: UUID): UserDto {
        val user = userService.getUserById(id)
        return UserDto(user.username, user.bio)
    }

    @GetMapping("/users/admin/id")
    fun getUserByIdForAdmin(@RequestParam id: UUID): UserEntity {
        return userService.getUserById(id)
    }

    @GetMapping("/users/all")
    fun getAllUsers(): List<UserEntity> {
        return userService.getAllUsers()
    }

    @DeleteMapping("/users/delete")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun deleteUser(@RequestParam id: UUID) {
        userService.deleteUser(id)
    }

    @PutMapping("/users/update")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun updateUser(@RequestParam id: UUID, @RequestBody userDto: UserDto) {
        userService.updateUser(id, userDto)
    }
}