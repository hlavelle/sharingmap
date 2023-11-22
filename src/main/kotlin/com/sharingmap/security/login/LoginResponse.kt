package com.sharingmap.security.login

class LoginResponse (val username: String,
                     val email: String,
                     val enabled: Boolean,
                     val refreshToken: String,
                     val accessToken: String,
                     val accessTokenType: String = "Bearer")
