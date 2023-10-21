package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import com.sharingmap.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService
    ): LoginService {

    val LOGGER = LoggerFactory.getLogger(LoginServiceImpl::class.java)

    override fun login(email: String, password: String): UserEntity {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))

        return userService.retrieveFromCache(email) as UserEntity
    }

}