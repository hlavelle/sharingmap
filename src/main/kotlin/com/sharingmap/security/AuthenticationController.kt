package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import com.sharingmap.security.confirmationtoken.ConfirmationRequest
import com.sharingmap.security.confirmationtoken.ConfirmationTokenService
import com.sharingmap.security.jwt.*
import com.sharingmap.security.login.LoginRequest
import com.sharingmap.security.login.LoginResponse
import com.sharingmap.security.login.LoginService
import com.sharingmap.security.jwt.TokenRefreshResponse
import com.sharingmap.security.registration.RegistrationRequest
import com.sharingmap.security.registration.RegistrationResponse
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class AuthenticationController (
    private val authenticationService: AuthenticationService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val loginService: LoginService,
    private val refreshTokenService: RefreshTokenService,
    private val confirmationTokenService: ConfirmationTokenService
) {

    val LOGGER = LoggerFactory.getLogger(AuthenticationController::class.java)
    @PostMapping("/signup")
    fun createUser(@RequestBody request: RegistrationRequest, response: HttpServletResponse): ResponseEntity<Any> {
        return try {
            val user = authenticationService.createUser(request)
            val confirmationToken = confirmationTokenService.createConfirmationToken(user)
            ResponseEntity.ok(RegistrationResponse(
                email = user.email,
                enabled = user.enabled,
                confirmationTokenId = confirmationToken.id.toString()
            ))
        } catch (e: Exception) {
            LOGGER.error(e.localizedMessage)
            ResponseEntity.badRequest().body("Registration failed. Wrong format email or email already exists")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, response: HttpServletResponse): ResponseEntity<Any> {
        return try {
            val user = loginService.login(loginRequest.email, loginRequest.password)
            val authToken = setAuthToken(user, response)
            val refreshToken = user.id?.let { refreshTokenService.createRefreshToken(it) }
            ResponseEntity.ok(refreshToken?.let {
                LoginResponse(user.username, user.email, user.enabled,
                    it, authToken)
            })
        } catch (e: java.lang.Exception) {
            LOGGER.error(e.localizedMessage)
            ResponseEntity.badRequest().body("Login failed")
        }
    }

    @PostMapping("/signup/confirm")
    fun confirmEmailToken(@RequestBody request: ConfirmationRequest): String {
        return authenticationService.confirmToken(request.token, request.tokenId)
    }

    @GetMapping("/current")
    fun current(): UserEntity? {
        try {
            return SecurityContextHolder.getContext().authentication.principal as UserEntity
        } catch (e: NullPointerException) {
            LOGGER.error(e.localizedMessage)
        }
        return null
    }

    fun setAuthToken(user: UserEntity, response: HttpServletResponse): String {
        return user.email.let { jwtTokenProvider.createAuthToken(it, user.role) }
    }

    @PostMapping("/refreshtoken")
    fun refreshtoken(@RequestBody request: @Valid TokenRefreshRequest): ResponseEntity<*>? {
            val requestRefreshToken: String = request.refreshToken
            val tokenEntity = refreshTokenService.findByToken(requestRefreshToken)
        return if (tokenEntity != null) {
            refreshTokenService.verifyExpiration(tokenEntity)
            val user = tokenEntity.user
            val accessToken = jwtTokenProvider.createAuthToken(user.email, user.role)

            ResponseEntity.ok<Any?>(TokenRefreshResponse(requestRefreshToken, accessToken))
        } else {
            throw TokenRefreshException(
                requestRefreshToken,
                "Refresh token is not in database!")
        }
    }

    @GetMapping("/logout")
    fun logout(@RequestParam("id") userId: UUID) : ResponseEntity<Any>{
        refreshTokenService.deleteByUserId(userId)
        return ResponseEntity.ok().body("You logged out")
    }
}

