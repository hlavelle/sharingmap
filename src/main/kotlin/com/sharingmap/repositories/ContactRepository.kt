package com.sharingmap.repositories

import com.sharingmap.entities.ContactEntity
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactRepository: JpaRepository<ContactEntity, UUID>{
    fun findAllByUserId(userId: UUID): List<ContactEntity>
}