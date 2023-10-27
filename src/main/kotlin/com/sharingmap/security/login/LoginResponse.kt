package com.sharingmap.security.login

import com.sharingmap.entities.UserEntity

class LoginResponse (val username: String,
                     val email: String,
                     val enabled: Boolean,
                     val accessToken: String,
                     val refreshToken: String)
