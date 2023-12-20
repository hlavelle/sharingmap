package com.sharingmap.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserAdminController(private val userService: UserService) {
    @GetMapping("/admin/users/{id}")
    fun adminGetUserById(@PathVariable id: UUID): ResponseEntity<Any> {
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

    @GetMapping("/admin/users/all")
    fun adminGetAllUsers(): ResponseEntity<Any> {
        return try {
            val users = userService.getAllUsers()
            ResponseEntity.ok(users)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @DeleteMapping("/admin/users/delete/{userId}")
    fun adminDeleteUser(@PathVariable userId: UUID): ResponseEntity<Any> {
        return try {
            userService.deleteUser(userId)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/admin/users/update/{userId}")
    fun adminUpdateUser(@PathVariable userId: UUID, @RequestBody userDto: UserDto): ResponseEntity<Any> {
        return try {
            userService.updateUser(userId, userDto)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }
}