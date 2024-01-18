package com.sharingmap.security.resetpassword

import com.sharingmap.user.UserEntity
import java.util.*

enum class ConfirmResult {
    Ok,
    Expired,
    Failed
}
interface PasswordTokenService {
    fun savePasswordToken(token: PasswordTokenEntity)
    fun getRandomString() : String
    fun createPasswordToken(user: UserEntity): PasswordTokenEntity
    fun confirmToken(token: String, tokenId: String, deleteConfirmationToken: Boolean): ConfirmResult
    fun getToken(tokenId: UUID): Optional<PasswordTokenEntity>
    fun deleteToken(id: UUID): String
    fun deletePasswordTokenByUser(user: UserEntity)
}