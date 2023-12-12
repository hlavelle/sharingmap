package com.sharingmap.contact

import com.sharingmap.contact.ContactEntity
import com.sharingmap.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactRepository: JpaRepository<ContactEntity, UUID>{
    fun findAllByUser(user: UserEntity): List<ContactEntity>
}