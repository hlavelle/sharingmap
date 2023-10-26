package com.sharingmap.security.confirmationtoken

import org.springframework.stereotype.Service
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