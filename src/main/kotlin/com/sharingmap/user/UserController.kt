package com.sharingmap.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException
import java.util.UUID

@RestController
class UserController(private val userService: UserService) {

    @GetMapping("/users/myself")
    fun getMyInfoById(): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
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

            val userInfo = GetOtherUserDto(
                    id = id,
                    username = user.username,
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

    @DeleteMapping("/users/delete")
    fun deleteUser(): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            user.id?.let { userService.makeUserDisabled(it) }
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/users/update")
    fun updateUser(@RequestBody userDto: UserDto): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            user.id?.let { userService.updateUser(it, userDto) }
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