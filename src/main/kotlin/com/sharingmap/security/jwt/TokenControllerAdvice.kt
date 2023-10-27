package com.sharingmap.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.*


@RestControllerAdvice
class TokenControllerAdvice {
    @ExceptionHandler(value = arrayOf(TokenRefreshException::class))
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleTokenRefreshException(ex: TokenRefreshException, request: WebRequest): ErrorMessage? {
        return ex.message?.let {
            ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                Date(),
                it,
                request.getDescription(false)
            )
        }
    }
}