package com.sharingmap.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service


@Service
class JwtServiceImpl : JwtService {
    override fun extractUserName(token: String?): String? {
        TODO("Not yet implemented")
    }

    override fun generateToken(userDetails: UserDetails?): String? {
        TODO("Not yet implemented")
    }

    override fun isTokenValid(token: String?, userDetails: UserDetails?): Boolean {
        TODO("Not yet implemented")
    }

}