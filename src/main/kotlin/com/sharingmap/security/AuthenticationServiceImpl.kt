package com.sharingmap.security

import com.sharingmap.user.Role
import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserRepository
import com.sharingmap.security.confirmationtoken.ConfirmationTokenEntity
import com.sharingmap.security.confirmationtoken.ConfirmationTokenService
import com.sharingmap.security.email.EmailValidator
import com.sharingmap.security.jwt.JwtTokenProvider
import com.sharingmap.security.jwt.RefreshTokenService
import com.sharingmap.security.login.LoginResponse
import com.sharingmap.security.registration.RegistrationRequest
import com.sharingmap.user.UserService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class AuthenticationServiceImpl(private val userRepository: UserRepository,
                                private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                                private val emailValidator: EmailValidator,
                                private val confirmationTokenService: ConfirmationTokenService,
                                private val userService: UserService,
                                private val jwtTokenProvider: JwtTokenProvider,
                                private val refreshTokenService: RefreshTokenService
) : AuthenticationService {

    override fun createUser(request: RegistrationRequest): UserEntity {
        val isValidEmail = request.email.let { emailValidator.test(it) }
        if (!isValidEmail) throw IllegalStateException("Email isn't valid.")

        val userFromDB: UserEntity? = userRepository.findByEmail(request.email)

        if (userFromDB != null) {
            if (!userFromDB.enabled) {
                userFromDB.id.let { userService.deleteUser(it) }
            } else {
                throw IllegalStateException("Email already taken.") //TODO сделать нормальные исключения
            }
        }
        val user = UserEntity(request.username, request.email, Role.ROLE_USER, bCryptPasswordEncoder.encode(request.password))
        userRepository.save(user)
        return user
    }

    @Transactional
    override fun confirmToken(token: String, tokenId: String): Any {
        val confirmationToken: Optional<ConfirmationTokenEntity> = confirmationTokenService.getToken(UUID.fromString(tokenId))

        return if (confirmationToken.isPresent && confirmationToken.get().token == token) {
            val user: UserEntity = confirmationToken.get().user
            val expiredAt: LocalDateTime? = confirmationToken.get().expiresAt
            if (expiredAt != null) {
                check(!expiredAt.isBefore(LocalDateTime.now())) { "token expired" }
            }
            user.enabled = true
            val authentication = UsernamePasswordAuthenticationToken(user.email, null, user.authorities)
            SecurityContextHolder.getContext().authentication = authentication

            val authToken = jwtTokenProvider.createAuthToken(user.email, user.role)
            val refreshToken = user.id?.let { refreshTokenService.createRefreshToken(it) }

            confirmationTokenService.deleteToken(UUID.fromString(tokenId))

            if (refreshToken != null) {
                LoginResponse(user.username, user.email, user.enabled,
                    refreshToken, authToken)
            } else {
                throw IllegalStateException("refresh token didn't create")
            }
        } else {
            throw IllegalStateException("can't confirm")
        }
    }
}