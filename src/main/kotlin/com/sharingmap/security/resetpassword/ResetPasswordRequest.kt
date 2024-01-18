package com.sharingmap.security.resetpassword

class CheckTokenRequest (
    val token: String,
    val tokenId: String)

class ResetPasswordRequest (
    val token: String,
    val tokenId: String,
    val userId: String,
    val password: String)
