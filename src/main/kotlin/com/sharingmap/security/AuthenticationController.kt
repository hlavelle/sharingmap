package com.sharingmap.security

import com.sharingmap.city.CityNotFoundException
import com.sharingmap.user.UserEntity
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
import okhttp3.internal.http.HTTP_NOT_FOUND
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
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
    @PostMapping("/signup") //TODO для анонима или админа
    fun createUser(@RequestBody request: RegistrationRequest, response: HttpServletResponse): ResponseEntity<Any> {
        return try {
            val user = authenticationService.createUser(request)
            val confirmationToken = confirmationTokenService.createConfirmationToken(user)
            ResponseEntity.ok(RegistrationResponse(
                confirmationTokenId = confirmationToken.id.toString()
            ))
        } catch (e: IllegalStateException) {
            LOGGER.error(e.localizedMessage)
            ResponseEntity.badRequest().body("Registration failed. ${e.localizedMessage}")
        } catch (e: Exception) {
            LOGGER.error(e.localizedMessage)
            ResponseEntity.badRequest().body("Registration failed. Wrong format email or email already exists")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, response: HttpServletResponse): ResponseEntity<Any> {
        return try {
            val user = loginService.login(loginRequest.email, loginRequest.password)
            val authToken = jwtTokenProvider.createAuthToken(user.email, user.role)
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
    fun confirmEmailToken(@RequestBody request: ConfirmationRequest): ResponseEntity<Any> {
        val response = authenticationService.confirmToken(request.token, request.tokenId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/signup/confirm/resentConfirmationToken")
    fun resendConfirmationToken(@RequestParam("email") email: String): Any {
        val confirmationToken = confirmationTokenService.resendToken(email)
        if (confirmationToken != null) {
            return ResponseEntity.ok(RegistrationResponse(
                confirmationTokenId = confirmationToken.id.toString()
            ))
        }
        return ResponseEntity.status(HTTP_NOT_FOUND)
    }

    @GetMapping("/current") //TODO подумать, что с этим делать
    fun current(): UserEntity? {
        try {
            return SecurityContextHolder.getContext().authentication.principal as UserEntity
        } catch (e: NullPointerException) {
            LOGGER.error(e.localizedMessage)
        }
        return null
    }

    @GetMapping("/is_auth")
    fun isAuth(): ResponseEntity<Any>? {
        try {
            if (SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.ok()
            }
            ResponseEntity.badRequest()
        } catch (e: NullPointerException) {
            LOGGER.error(e.localizedMessage)
        }
        return null
    }

    @PostMapping("/refreshToken")
    fun refreshToken(@RequestBody request: @Valid TokenRefreshRequest): ResponseEntity<*>? {
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

