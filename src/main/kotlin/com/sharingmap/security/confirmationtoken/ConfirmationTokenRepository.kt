package com.sharingmap.security.confirmationtoken

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional(readOnly = true)
interface ConfirmationTokenRepository: JpaRepository<ConfirmationTokenEntity, UUID> {
//    override fun findById(tokenId: UUID) : ConfirmationTokenEntity?
}