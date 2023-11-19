package com.sharingmap.security.resetpassword

import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import com.sharingmap.security.email.EmailServiceImpl
import com.sharingmap.security.email.EmailValidator
import com.sharingmap.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ResetPasswordController(
    private val emailValidator: EmailValidator,
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val passwordTokenService: PasswordTokenService
) {

    val logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)
    @PostMapping("/resetPassword")
    fun resetPassword(@RequestBody request: SendPasswordTokenRequest): Any {
        val isValidEmail = emailValidator.test(request.email)
        if (!isValidEmail) throw IllegalStateException("email isn't valid")

        val user: UserEntity = userRepository.findByEmail(request.email)
            ?: throw IllegalStateException("User with this email doesn't exists")

        val token = passwordTokenService.createPasswordToken(user)
        return ResponseEntity.ok(
            ResetPasswordResponse(
                resetPasswordTokenId = token.id.toString(),
                userId = user.id.toString()
        ))
    }

    @PostMapping("/resetPassword/change")
    fun confirmPasswordToken(@RequestBody request: ResetPasswordRequest): String {
        passwordTokenService.confirmToken(request.token, request.tokenId)

        userService.changePassword(UUID.fromString(request.userId), request.password)
        return "password changed"
    }
}