package com.sharingmap.security.jwt

import jakarta.validation.constraints.NotBlank


class TokenRefreshRequest(
    @NotBlank
    val refreshToken: String
    )