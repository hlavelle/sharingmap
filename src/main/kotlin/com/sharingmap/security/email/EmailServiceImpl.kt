package com.sharingmap.security.email

import jakarta.mail.MessagingException
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
@Async
class EmailServiceImpl(private val javaMailSender: JavaMailSender): EmailService {

    val LOGGER = LoggerFactory.getLogger(EmailServiceImpl::class.java)
    override fun send(to: String, email: String) {
        try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "utf-8")
            helper.setText(email, true)
            helper.setTo(to)
            helper.setSubject("Confirm your email")
            helper.setFrom("hello@sharingmap.com")
            javaMailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            LOGGER.error("failed to send email", e)
            throw IllegalStateException("failed to send email")
        }
    }
}