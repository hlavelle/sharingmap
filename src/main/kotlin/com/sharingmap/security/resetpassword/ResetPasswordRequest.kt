package com.sharingmap.security.resetpassword

class ResetPasswordRequest (val token: String, val tokenId: String, val userId: String, val password: String)