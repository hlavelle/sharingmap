package com.sharingmap.image

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ItemImageRepository : JpaRepository<ItemImageEntity, UUID> {
    fun findAllByEntityId(itemId: UUID): List<ItemImageEntity>

//    @Query("UPDATE images SET is_loaded = true WHERE item_id = ?itemId")
//    fun setImageUploaded(itemId: UUID): Int
}

@Repository
interface UserImageRepository : JpaRepository<UserImageEntity, UUID> {
    fun findAllByEntityId(itemId: UUID): List<UserImageEntity>

//    @Query("UPDATE images SET is_loaded = true WHERE item_id = ?itemId")
//    fun setImageUploaded(itemId: UUID): Int
}