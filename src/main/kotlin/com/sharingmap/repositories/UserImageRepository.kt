package com.sharingmap.repositories

import com.sharingmap.entities.UserImageEntity
import org.hibernate.annotations.SQLUpdate
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserImageRepository : JpaRepository<UserImageEntity, UUID> {
    fun findAllByEntityId(itemId: UUID): List<UserImageEntity>

//    @Query("UPDATE images SET is_loaded = true WHERE item_id = ?itemId")
//    fun setImageUploaded(itemId: UUID): Int
}