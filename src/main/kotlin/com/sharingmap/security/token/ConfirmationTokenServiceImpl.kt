package com.sharingmap.security.token

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ConfirmationTokenServiceImpl(private val confirmationTokenRepository: ConfirmationTokenRepository)
    : ConfirmationTokenService {

    override fun saveConfirmationToken(token: ConfirmationTokenEntity) {
        confirmationTokenRepository.save(token)
    }

    override fun getToken(token: String): Optional<ConfirmationTokenEntity> {
        return confirmationTokenRepository.findByToken(token)
    }

    override fun deleteToken(id: UUID): String {
        confirmationTokenRepository.deleteById(id)
        return "token deleted"
    }
}