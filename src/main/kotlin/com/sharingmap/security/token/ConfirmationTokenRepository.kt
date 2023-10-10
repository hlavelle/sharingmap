package com.sharingmap.security.token

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
@Transactional(readOnly = true)
interface ConfirmationTokenRepository: JpaRepository<ConfirmationTokenEntity, UUID> {

    fun findByToken(token: String) : Optional<ConfirmationTokenEntity>
}