package com.sharingmap.services

import com.sharingmap.entities.Role
import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(private val userRepository: UserRepository,
                                private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : AuthenticationService {

    override fun createUser(user: UserEntity): Boolean {
        val userFromDB: UserEntity? = userRepository.findByEmail(user.email)

        if (userFromDB != null) {
            return false
        }

        user.assignRole(Role.ROLE_USER)
        user.setPassword(bCryptPasswordEncoder.encode(user.password))
        userRepository.save(user)
        return true
    }
}