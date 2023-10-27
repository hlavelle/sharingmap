package com.sharingmap.security.login

import com.sharingmap.entities.UserEntity

class LoginResponse (val username: String,
                     val email: String,
                     val enabled: Boolean,
                     val refreshToken: String,
                     val accessToken: String,
                     val accessTokenType: String = "Bearer")
