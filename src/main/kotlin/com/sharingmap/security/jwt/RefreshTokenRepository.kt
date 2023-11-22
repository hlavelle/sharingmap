package com.sharingmap.security.jwt

import com.sharingmap.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import java.util.*


interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByToken(token: String): RefreshTokenEntity?

    @Modifying
    fun deleteByUser(user: UserEntity): Int
}