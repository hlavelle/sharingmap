package com.sharingmap.repositories

import com.sharingmap.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(email: String?): UserEntity?
}