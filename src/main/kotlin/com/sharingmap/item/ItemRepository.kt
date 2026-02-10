package com.sharingmap.item

import com.sharingmap.user.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

@Repository
interface ItemRepository : JpaRepository <ItemEntity, UUID> {
    fun findAllByUserIdAndState(userId: UUID, state: State, pageable: Pageable): Page<ItemEntity>
    fun findAllByUser(user: UserEntity): List<ItemEntity>

    @EntityGraph(attributePaths = [
        "user",
        "categories",
        "images",
        "locations",
        "subcategory",
        "city"
    ])
    @Query("""
        SELECT DISTINCT i FROM ItemEntity i
        JOIN i.user u
        WHERE i.state = :state
        AND u.enabled = :enabled
        AND (
            :query = '' OR
            LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(i.text, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        )
        AND (:categoryId = 0L OR EXISTS (
            SELECT 1 FROM i.categories c WHERE c.id = :categoryId
        ))
        AND (:subcategoryId = 0L OR i.subcategory.id = :subcategoryId)
        AND (:cityId = 0L OR i.city.id = :cityId)
    """)
    fun findActiveItemsByFilters(
        @Param("categoryId") categoryId: Long,
        @Param("subcategoryId") subcategoryId: Long,
        @Param("cityId") cityId: Long,
        @Param("query") query: String,
        @Param("state") state: State,
        @Param("enabled") enabled: Boolean,
        pageable: Pageable
    ): Page<ItemEntity>
}
