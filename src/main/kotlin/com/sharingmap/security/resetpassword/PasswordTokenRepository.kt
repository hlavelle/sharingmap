package com.sharingmap.security.resetpassword

import com.sharingmap.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(readOnly = true)
interface PasswordTokenRepository: JpaRepository<PasswordTokenEntity, UUID> {
    fun deleteByUser(user: UserEntity)
    fun findByUser(user: UserEntity): Optional<PasswordTokenEntity>
}