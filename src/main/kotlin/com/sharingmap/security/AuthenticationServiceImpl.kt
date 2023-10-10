package com.sharingmap.security

import com.sharingmap.entities.Role
import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import com.sharingmap.security.token.ConfirmationTokenEntity
import com.sharingmap.security.token.ConfirmationTokenService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthenticationServiceImpl(private val userRepository: UserRepository,
                                private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                                private val emailValidator: EmailValidator,
                                private val confirmationTokenService: ConfirmationTokenService
) : AuthenticationService {

    override fun createUser(user: UserEntity): String {
        val isValidEmail = user.email?.let { emailValidator.test(it) }
        if (!isValidEmail!!) throw IllegalStateException("email isn't valid") //TODO сделать нормальные исключения

        val userFromDB: UserEntity? = userRepository.findByEmail(user.email)

        if (userFromDB != null) {
            throw IllegalStateException("email already taken") //TODO сделать нормальные исключения
        }
        user.assignRole(Role.ROLE_USER)
        user.setPassword(bCryptPasswordEncoder.encode(user.password))
        userRepository.save(user)

        val token = UUID.randomUUID().toString()

        val confirmationToken =  ConfirmationTokenEntity(token, LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15), user)

        confirmationTokenService.saveConfirmationToken(confirmationToken)
        return token
    }

    @Transactional
    override fun confirmToken(token: String): String {
        val confirmationToken: Optional<ConfirmationTokenEntity> = confirmationTokenService.getToken(token)

        if (confirmationToken.isPresent) {
            val user: UserEntity = confirmationToken.get().user
            val expiredAt: LocalDateTime? = confirmationToken.get().expiresAt
            if (expiredAt != null) {
                check(expiredAt.isBefore(LocalDateTime.now())) { "token expired" }
            }
            user.enabled = true
            val tokenId = confirmationToken.get().id
            if (tokenId != null) {
                confirmationTokenService.deleteToken(tokenId)
            }
            return "confirmed"
        } else {
            return "can't confirm"
        }
    }
}