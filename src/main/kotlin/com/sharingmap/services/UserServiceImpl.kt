package com.sharingmap.services

import com.sharingmap.entities.UserEntity
import com.sharingmap.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun getUserById(id: Long): UserEntity = userRepository.findById(id).get()

    override fun getAllUsers(): List<UserEntity> = userRepository.findAll().toList()

    override fun createUser(user: UserEntity) {
        userRepository.save(user)
    }

    override fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    override fun updateUser(id: Long, user: UserEntity) {
        var newUser = userRepository.findById(id).get()
        newUser.name = user.name
        userRepository.save(newUser)
    }

//    fun <T : Any> Optional<out T>.toList(): List<T> =
//        if (isPresent) listOf(get()) else emptyList()
}