package com.sharingmap.security.email

import org.springframework.stereotype.Service
import java.util.function.Predicate

@Service
class EmailValidator : Predicate<String> {
    override fun test(t: String): Boolean {
        //TODO: Regex to validate email
        return true
    }
}