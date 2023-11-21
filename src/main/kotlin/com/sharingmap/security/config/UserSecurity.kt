package com.sharingmap.security.config

import com.sharingmap.entities.UserEntity
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
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
        val user = authentication.principal as UserEntity
        // Assuming your UserDetails has a method like getUserId()
        val userPrincipalId = user.id.toString()

        // Compare the user ID from the context with the user's actual ID
        return userPrincipalId == userId
    }

}