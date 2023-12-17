package com.sharingmap.security.email

interface EmailService {
    fun sendConfirmationLetter(to: String, email: String)

    fun sendResetPasswordLetter(to: String, email: String)
}