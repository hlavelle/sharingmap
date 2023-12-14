package com.sharingmap.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException
import java.util.UUID

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/myself")
    @PreAuthorize("#id == principal.id")
    fun getMyInfoById(@RequestParam id: UUID): ResponseEntity<Any> {
        return try {
            val user = userService.getUserById(id)
            val info = UserInfoDto(
            username = user.username,
            bio = user.bio ?: "",
            id = user.id!!,
            email = user.email,
            hasImage = user.image != null
            )
            ResponseEntity.ok(info)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<Any> {
        return try {
            val user = userService.getUserById(id)

            val userInfo = GetOtherUserDto(username = user.username,
                bio = user.bio?:"",
                hasImage = user.image != null)
            ResponseEntity.ok(userInfo)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @GetMapping("/users/admin/{id}")
    fun getUserByIdForAdmin(@PathVariable id: UUID): ResponseEntity<Any> {
        return try {
            val user = userService.getUserById(id)
            ResponseEntity.ok(user)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @GetMapping("/users/all")
    fun getAllUsers(): ResponseEntity<Any> {
        return try {
            val users = userService.getAllUsers()
            ResponseEntity.ok(users)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @DeleteMapping("/users/delete")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun deleteUser(@RequestParam id: UUID): ResponseEntity<Any> {
        return try {
            userService.deleteUser(id)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/users/update")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun updateUser(@RequestParam id: UUID, @RequestBody userDto: UserDto): ResponseEntity<Any> {
        return try {
            userService.updateUser(id, userDto)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }
}