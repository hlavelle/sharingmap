package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/signup")
    fun createUser(@RequestBody user: UserEntity): String {
        return authenticationService.createUser(user)
    }

    @GetMapping("/signup/confirm")
    fun confirmEmailToken(@RequestParam("token") token: String): String {
        return authenticationService.confirmToken(token)
    }
}