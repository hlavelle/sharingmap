package com.sharingmap.security.email

import org.springframework.stereotype.Service
import java.util.function.Predicate

@Service
class EmailValidator : Predicate<String> {
    private val emailRegex: Regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$".toRegex()

    override fun test(email: String): Boolean {
        return emailRegex.matches(email)
    }
}