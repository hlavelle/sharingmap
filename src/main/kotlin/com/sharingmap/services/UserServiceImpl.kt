package com.sharingmap.services

import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun getUserById(id: UUID): UserEntity = userRepository.findById(id).get()

    override fun getAllUsers(): List<UserEntity> = userRepository.findAll().toList()

    override fun createUser(user: UserEntity) {
        userRepository.save(user)
    }

    override fun deleteUser(id: UUID) {
        userRepository.deleteById(id)
    }

    override fun updateUser(id: UUID, user: UserEntity) {
        val newUser = userRepository.findById(id).get()
        newUser.name = user.name
        newUser.bio = user.bio
        userRepository.save(newUser)
    }

//    fun <T : Any> Optional<out T>.toList(): List<T> =
//        if (isPresent) listOf(get()) else emptyList()
}