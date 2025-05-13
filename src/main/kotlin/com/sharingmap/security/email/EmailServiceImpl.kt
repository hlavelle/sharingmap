package com.sharingmap.security.email

import jakarta.mail.MessagingException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.retry.annotation.Recover

@Service
@Async
class EmailServiceImpl(private val javaMailSender: JavaMailSender,
        @Value("\${spring.mail.properties.mail.smtp.connectiontimeout:5000}")
        val connectionTimeout: Long): EmailService {

    val LOGGER = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    @Retryable(
        value = [MessagingException::class],
        maxAttempts = 3,
        backoff = Backoff(delayExpression = "#{@emailServiceImpl.connectionTimeout}")
    )
    override fun sendConfirmationLetter(to: String, email: String) {
        try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "utf-8")
            helper.setText(email, true)
            helper.setTo(to)
            helper.setSubject("Подтверждение почты SharingMap")
            helper.setFrom("info@sharingmap.ru")
            javaMailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            LOGGER.error("failed to send email", e)
            throw IllegalStateException("failed to send email")
        }
    }

    @Retryable(
        value = [MessagingException::class],
        maxAttempts = 3,
        backoff = Backoff(delayExpression = "#{@emailServiceImpl.connectionTimeout}")
    )
    override fun sendResetPasswordLetter(to: String, email: String) {
        try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "utf-8")
            helper.setText(email, true)
            helper.setTo(to)
            helper.setSubject("Забыли пароль SharingMap")
            helper.setFrom("info@sharingmap.ru")
            javaMailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            LOGGER.error("failed to send email", e)
            throw IllegalStateException("failed to send email")
        }
    }

    @Recover
    fun recover(e: MessagingException, to: String, email: String) {
        LOGGER.error("Giving up on sending email to $to after 3 attempts", e)
        throw IllegalStateException("failed to send email after 3 attempts", e)
    }
}