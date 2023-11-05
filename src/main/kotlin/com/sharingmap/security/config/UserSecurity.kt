package com.sharingmap.security.config

import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.stereotype.Component
import java.util.function.Supplier

@Component
class UserSecurity: AuthorizationManager<RequestAuthorizationContext> {
    override fun check(
        authentication: Supplier<Authentication>,
        context : RequestAuthorizationContext
    ): AuthorizationDecision? {
        val userId: String = context.variables["userId"].toString()
        return AuthorizationDecision(hasUserId(authentication.get(), userId))
    }

    fun hasUserId(authentication: Authentication, userId: String): Boolean {
        TODO("Not yet implemented")
    }

}