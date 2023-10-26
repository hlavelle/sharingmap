package com.sharingmap.security.confirmationtoken

import java.util.Optional
import java.util.UUID

interface ConfirmationTokenService {

    fun saveConfirmationToken(token: ConfirmationTokenEntity)
    fun getToken(token: String): Optional<ConfirmationTokenEntity>

    fun deleteToken(id: UUID): String
}