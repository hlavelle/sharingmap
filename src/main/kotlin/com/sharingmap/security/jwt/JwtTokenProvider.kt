package com.sharingmap.security.jwt

import aws.sdk.kotlin.runtime.auth.credentials.internal.sts.model.ExpiredTokenException
import com.sharingmap.entities.Role
import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import com.sharingmap.security.AuthenticationService
import com.sharingmap.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Base64
import java.util.Date

@Component
class JwtTokenProvider(
    private val userService: UserService,
    private val userRepository: UserRepository,

    @Value("\${auth.cookie.secret}")
    private var secretKey: String,
    @Value("\${auth.cookie.auth}")
    val authCookieName: String,
    @Value("\${auth.cookie.refresh}")
    val refreshCookieName: String,
    @Value("\${auth.cookie.expiration-auth}")
    val authExpirationCookie: Int,
    @Value("\${auth.cookie.expiration-refresh}")
    val refreshExpirationCookie: Int,
    @Value("\${auth.cookie.path}")
    val cookiePath: String,
) {
    val LOGGER = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    @PostConstruct
    fun init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun createAuthToken(email:String, role: Role?): String {
        val claims = Jwts.claims().setSubject(email)
        claims["role"] = role
        claims["user_id"] = userRepository.findByEmail(email)?.id.toString()
        val now = Date()
        val valid = Date(now.time + authExpirationCookie)
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(valid)
            .signWith(SignatureAlgorithm.HS256, secretKey).compact()
    }

    fun createRefreshToken(email:String, role: Role?): String {
        val claims = Jwts.claims().setSubject(email)
        claims["role"] = role
        claims["user_id"] = userRepository.findByEmail(email)?.id.toString()
        val now = Date()
        val valid = Date(now.time + refreshExpirationCookie)
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(valid)
            .signWith(SignatureAlgorithm.HS256, secretKey).compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            val claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return claimsJws.body.expiration.after(Date())
        } catch (e: ExpiredTokenException) {
            LOGGER.error(e.localizedMessage)
        }
        return false
    }

    fun getAuthentication(token: String): Authentication {
        val user = userService.loadUserByUsername(getUserEmail(token))
        return UsernamePasswordAuthenticationToken(user, user.password, user.authorities)
    }

    fun getUserEmail(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val cookies = request.cookies
        var res: String? = null
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name.equals(authCookieName)) {
                   res = cookie.value
                }
            }
        }
        return res
    }
}