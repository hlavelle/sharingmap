package com.sharingmap.controllers

import com.sharingmap.entities.UserEntity
import com.sharingmap.services.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/signup")
    fun createUser(@RequestBody user: UserEntity) {
        authenticationService.createUser(user)
    }
}