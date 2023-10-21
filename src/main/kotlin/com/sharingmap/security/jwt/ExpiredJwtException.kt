package com.sharingmap.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class ExpiredJwtException(token: String, message: String) : RuntimeException(String.format("Failed for [%s]: %s", token, message))