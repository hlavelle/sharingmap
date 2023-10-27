package com.sharingmap.security.jwt

import java.util.*

interface RefreshTokenService {
    fun findByToken(token: String): RefreshTokenEntity?
    fun createRefreshToken(userId: UUID): String
    fun verifyExpiration(token: RefreshTokenEntity): RefreshTokenEntity
    fun deleteByUserId(userId: UUID): Int
}