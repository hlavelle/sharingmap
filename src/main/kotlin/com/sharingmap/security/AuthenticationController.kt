package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import com.sharingmap.security.jwt.*
import com.sharingmap.security.login.LoginRequest
import com.sharingmap.security.login.LoginResponse
import com.sharingmap.security.login.LoginService
import com.sharingmap.security.jwt.TokenRefreshResponse
import com.sharingmap.security.registration.RegistrationRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
class AuthenticationController (
    private val authenticationService: AuthenticationService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val loginService: LoginService,
    private val refreshTokenService: RefreshTokenService
) {

    val LOGGER = LoggerFactory.getLogger(AuthenticationController::class.java)
    @PostMapping("/signup")
    fun createUser(@RequestBody request: RegistrationRequest, response: HttpServletResponse): ResponseEntity<Any> {
        return try {
            val user = authenticationService.createUser(request)
            ResponseEntity.ok(user)
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

    @GetMapping("/signup/confirm")
    fun confirmEmailToken(@RequestParam("token") token: String): String {
        return authenticationService.confirmToken(token)
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
            if (tokenEntity != null) {
                refreshTokenService.verifyExpiration(tokenEntity)
                val user = tokenEntity.user
                val accessToken = jwtTokenProvider.createAuthToken(user.email, user.role)

                return ResponseEntity.ok<Any?>(TokenRefreshResponse(requestRefreshToken, accessToken))
            }
            throw TokenRefreshException(
                    requestRefreshToken,
                    "Refresh token is not in database!")
            }
}

