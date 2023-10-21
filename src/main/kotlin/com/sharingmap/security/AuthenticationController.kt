package com.sharingmap.security

import com.sharingmap.entities.UserEntity
import com.sharingmap.security.jwt.JwtTokenProvider
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
class AuthenticationController (
    private val authenticationService: AuthenticationService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val loginService: LoginService) {

    val LOGGER = LoggerFactory.getLogger(AuthenticationController::class.java)
    @PostMapping("/signup")
    fun createUser(@RequestBody user: UserEntity, response: HttpServletResponse): String {
        return try {
            authenticationService.createUser(user)
        } catch (e: Exception) {
            LOGGER.error(e.localizedMessage)
            "error"
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, response: HttpServletResponse): String{
        return try {
            val user = loginService.login(loginRequest.email, loginRequest.password)
            val authToken = setAuthToken(user, response)
            val refreshToken = setRefreshToken(user, response)
            "$authToken:$refreshToken"
        } catch (e: java.lang.Exception) {
            LOGGER.error(e.localizedMessage)
            clearAuthAndRefreshTokens(response)
            "error"
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

    fun setAuthToken(user: UserEntity?, response: HttpServletResponse): String? {
        val jwtToken = user?.email?.let { jwtTokenProvider.createAuthToken(it, user.role) }
        val cookie = Cookie(jwtTokenProvider.authCookieName, jwtToken)
        cookie.path = jwtTokenProvider.cookiePath
        cookie.isHttpOnly = true
        cookie.maxAge = jwtTokenProvider.authExpirationCookie
        response.addCookie(cookie)
        return jwtToken
    }

    fun setRefreshToken(user: UserEntity?, response: HttpServletResponse): String? {
        val jwtToken = user?.email?.let { jwtTokenProvider.createRefreshToken(it, user.role) }
        val cookie = Cookie(jwtTokenProvider.refreshCookieName, jwtToken)
        cookie.path = jwtTokenProvider.cookiePath
        cookie.isHttpOnly = true
        cookie.maxAge = jwtTokenProvider.refreshExpirationCookie
        response.addCookie(cookie)
        return jwtToken
    }

    fun clearAuthAndRefreshTokens(response: HttpServletResponse) {
        val authCookie = Cookie(jwtTokenProvider.authCookieName, "-")
        authCookie.path = jwtTokenProvider.cookiePath

        val refreshCookie = Cookie(jwtTokenProvider.refreshCookieName, "-")
        refreshCookie.path = jwtTokenProvider.cookiePath

        response.addCookie(authCookie)
        response.addCookie(refreshCookie)
    }
}