package com.sharingmap.security.jwt

import com.sharingmap.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class RefreshTokenServiceImpl(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,

    @Value("\${sharingmap.app.expiration-refresh}")
    val refreshTokenDuration: Int
) : RefreshTokenService {
    override fun findByToken(token: String): RefreshTokenEntity? {
        return refreshTokenRepository.findByToken(token)
    }

    @Transactional
    @Synchronized
    override fun createRefreshToken(userId: UUID): String {
        val user = userRepository.findById(userId).get()
        val newTokenValue = UUID.randomUUID().toString()
        val newExpiry = Instant.now().plusSeconds(refreshTokenDuration.toLong())
        var refreshToken = refreshTokenRepository.findByUserId(userId)
            ?: RefreshTokenEntity(
                newTokenValue,
                user,
                newExpiry
            )
        refreshToken.token = newTokenValue
        refreshToken.expiryDate = newExpiry
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken.token
    }

    override fun verifyExpiration(token: RefreshTokenEntity): RefreshTokenEntity {
        if (token.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(token)
            throw TokenRefreshException(token.token, "Refresh token was expired. Please make a new signin request")
        }
        return token
    }

    @Transactional
    override fun deleteByUserId(userId: UUID): Int {
        return refreshTokenRepository.deleteByUserId(userId)
    }
}
