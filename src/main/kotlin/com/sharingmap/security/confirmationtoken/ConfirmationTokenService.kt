package com.sharingmap.security.confirmationtoken

import com.sharingmap.user.UserEntity
import java.util.Optional
import java.util.UUID

interface ConfirmationTokenService {
    fun saveConfirmationToken(token: ConfirmationTokenEntity)

    fun resendToken(email: String): ConfirmationTokenEntity?

    fun createConfirmationToken(user: UserEntity): ConfirmationTokenEntity

    fun getToken(tokenId: UUID): Optional<ConfirmationTokenEntity>
    fun deleteToken(id: UUID): String
    fun deleteConfirmationTokenByUser(user: UserEntity)
    fun getRandomString() : String
}