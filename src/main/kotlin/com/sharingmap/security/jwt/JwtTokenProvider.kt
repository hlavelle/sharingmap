package com.sharingmap.security.jwt

import com.sharingmap.entities.Role
import com.sharingmap.repositories.UserRepository
import com.sharingmap.services.UserService
import io.jsonwebtoken.*
import jakarta.annotation.PostConstruct
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

    @Value("\${sharingmap.app.secret}")
    private var secretKey: String,
    @Value("\${sharingmap.app.expiration-auth}")
    val authExpirationCookie: Int,
    @Value("\${sharingmap.app.path}")
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

    fun validateToken(token: String): Boolean {
        try {
            val claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return claimsJws.body.expiration.after(Date())
        } catch (e: ExpiredJwtException) {
            LOGGER.error("JWT token is expired: {}", e.localizedMessage)
        }catch (e: SignatureException) {
            LOGGER.error("Invalid JWT signature: {}", e.localizedMessage)
        } catch (e: MalformedJwtException) {
            LOGGER.error("Invalid JWT token: {}", e.localizedMessage)
        } catch (e: UnsupportedJwtException) {
            LOGGER.error("JWT token is unsupported: {}", e.localizedMessage)
        } catch (e: IllegalArgumentException) {
            LOGGER.error("JWT claims string is empty: {}", e.localizedMessage)
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
}