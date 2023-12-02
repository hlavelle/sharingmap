package com.sharingmap.user

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/info")
    fun getUserInfoById(@RequestParam id: UUID): UserInfoDto {
        val user = userService.getUserById(id)

        return UserInfoDto(username = user.username,
            bio = user.bio?:"",
            id = user.id!!,
            email = user.email,
            hasImage = user.image != null)
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
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id") // TODO think about id
    fun updateUser(@RequestParam id: UUID, @RequestBody userUpdateDto: UserUpdateDto) {
        val userDto: UserDto = UserDto(bio = userUpdateDto.bio, username = userUpdateDto.username)
        userService.updateUser(userUpdateDto.id, userDto)
    }
}