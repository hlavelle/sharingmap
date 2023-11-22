package com.sharingmap.security.resetpassword

import com.sharingmap.user.UserEntity
import java.util.*

interface PasswordTokenService {
    fun savePasswordToken(token: PasswordTokenEntity)
    fun getRandomString() : String
    fun createPasswordToken(user: UserEntity): PasswordTokenEntity
    fun confirmToken(token: String, tokenId: String): String
    fun getToken(tokenId: UUID): Optional<PasswordTokenEntity>
    fun deleteToken(id: UUID): String
    fun deletePasswordTokenByUser(user: UserEntity)
}