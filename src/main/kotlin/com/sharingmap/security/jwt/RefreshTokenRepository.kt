package com.sharingmap.security.jwt

import com.sharingmap.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*


interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByToken(token: String): RefreshTokenEntity?
    fun findByUserId(userId: UUID): RefreshTokenEntity?

    @Modifying
    fun deleteByUser(user: UserEntity): Int

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.user.id = :userId")
    fun deleteByUserId(@Param("userId") userId: UUID): Int
}
