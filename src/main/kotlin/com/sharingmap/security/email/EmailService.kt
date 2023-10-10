package com.sharingmap.security.email

interface EmailService {
    fun send(to: String, email: String)
}