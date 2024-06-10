package com.sharingmap.security

import com.sharingmap.security.jwt.*
import com.sharingmap.security.login.LoginService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.ui.Model


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
    @PostMapping("/login1")
    fun login1(form: Form, model: Model): String {
//        println("sdasdasd")
//        return "ok"
        try {
            val user = loginService.login(form.username, form.password)
            val authToken = jwtTokenProvider.createAuthToken(user.email, user.role)
            val refreshToken = user.id?.let { refreshTokenService.createRefreshToken(it) }
            return ""
//            ResponseEntity.ok(refreshToken?.let {
//                LoginResponse(user.username, user.email, user.enabled,
//                    it, authToken)
//            })
        } catch (e: java.lang.Exception) {
            return "Error"
//            LOGGER.error(e.localizedMessage)
//            ResponseEntity.badRequest().body("Login failed")
        }
    }
}
