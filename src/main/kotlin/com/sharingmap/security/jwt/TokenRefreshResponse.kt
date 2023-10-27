package com.sharingmap.security.jwt

class TokenRefreshResponse(val refreshToken: String,
                           val accessToken: String,
                           val accessTokenType: String = "Bearer")