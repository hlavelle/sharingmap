package com.sharingmap.services

import com.sharingmap.entities.Role
import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import org.springframework.security.authentication.CachingUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun getUserById(id: UUID): UserEntity = userRepository.findById(id).get()

    override fun getAllUsers(): List<UserEntity> = userRepository.findAll().toList()


    override fun deleteUser(id: UUID) {
        userRepository.deleteById(id)
    }

    override fun updateUser(id: UUID, user: UserEntity) {
        val newUser = userRepository.findById(id).get()
        //newUser.username = user.username
        newUser.bio = user.bio
        userRepository.save(newUser)
    }

    override fun loadUserByUsername(email: String?): UserDetails {
        return userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
    }

    override fun retrieveFromCache(email: String): UserDetails {
        return CachingUserDetailsService(this).loadUserByUsername(email)
    }
}