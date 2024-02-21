package com.sharingmap.user

import com.sharingmap.item.ItemRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val itemRepository: ItemRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
    ) : UserService {

    override fun getUserById(id: UUID): UserEntity {
        return userRepository.findById(id).orElseThrow { UserNotFoundException("User not found with ID: $id") }
    }

    override fun getAllUsers(): List< UserEntity> = userRepository.findAll().toList()


    override fun deleteUser(userId: UUID) { //TODO не работает из-за ManyToMany у категории и локации
        val user = getUserById(userId)
        val userItems = ArrayList(itemRepository.findAllByUser(user))

        val iterator = userItems.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            iterator.remove()

            item.user = null
            itemRepository.delete(item)
        }
        userRepository.delete(user)
    }

    override fun updateUser(userId: UUID, userDto: UserDto) {
        val newUser = getUserById(userId)
        if (userDto.username != null) newUser.username = userDto.username!!
        if (userDto.bio != null) newUser.bio = userDto.bio
        userRepository.save(newUser)
    }

    override fun changePassword(id: UUID, password: String) {
        val newUser = userRepository.findById(id).get()
        newUser.password = bCryptPasswordEncoder.encode(password)
        userRepository.save(newUser)
    }

    override fun loadUserByUsername(email: String?): UserDetails {
        return userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User not found")
    }
}