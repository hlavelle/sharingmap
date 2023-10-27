package com.sharingmap.security.login

import com.sharingmap.entities.UserEntity

class LoginResponse (val name: String,
                     val email: String,
                     val enabled: Boolean,
                     val accessToken: String,
                     val refreshToken: String)
