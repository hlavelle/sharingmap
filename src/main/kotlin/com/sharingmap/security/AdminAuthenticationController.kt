package com.sharingmap.security

import com.sharingmap.security.jwt.*
import com.sharingmap.security.login.LoginService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import com.sharingmap.security.login.LoginResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.PostMapping
import jakarta.servlet.http.Cookie

@Controller
class AdminAuthController(
    private val authenticationService: AuthenticationService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val loginService: LoginService,
    private val refreshTokenService: RefreshTokenService
){
    val LOGGER = LoggerFactory.getLogger(AuthenticationController::class.java)

    @GetMapping("/login1")
    fun login1(): String {
        return "login1"
    }
    class Form {
        var username: String = ""
        var password: String = ""
    }
    @PostMapping("/loginValidate")
    fun login1(@ModelAttribute form: Form, response: HttpServletResponse): ResponseEntity<Any> {
        return try {
            val user = loginService.login(form.username, form.password)
            val authToken = jwtTokenProvider.createAuthToken(user.email, user.role)
            val refreshToken = user.id?.let { refreshTokenService.createRefreshToken(it) }

            // Create a cookie for the auth token
            val authTokenCookie = Cookie("accessToken", authToken).apply {
                isHttpOnly = true
                secure = false // Set to true if serving over HTTPS
                path = "/"
                maxAge = 3600000 // 1 hour duration in seconds
            }
            response.addCookie(authTokenCookie)

            // Optionally, create a cookie for the refresh token
            val refreshTokenCookie = refreshToken?.let {
                Cookie("refreshToken", it).apply {
                    isHttpOnly = true
                    secure = false // Set to true if serving over HTTPS
                    path = "/"
                    maxAge = 8640000 // 1 day duration in seconds
                }
            }
            refreshTokenCookie?.let { response.addCookie(it) }

            ResponseEntity.ok(refreshToken?.let {
                LoginResponse(user.username, user.email, user.enabled, it, authToken)
            })
        } catch (e: Exception) {
            println(e.localizedMessage)
            ResponseEntity.badRequest().body("Login failed")
        }
    }
}
