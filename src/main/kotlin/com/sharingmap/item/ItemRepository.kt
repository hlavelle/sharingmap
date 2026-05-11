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
    fun findAllByUser(user: UserEntity): List<ItemEntity>

    @Query("""
        SELECT i.id FROM ItemEntity i
        JOIN i.user u
        WHERE i.state = :state
        AND u.enabled = :enabled
        AND (:categoryId = 0L OR EXISTS (
            SELECT 1 FROM i.categories c WHERE c.id = :categoryId
        ))
        AND (:subcategoryId = 0L OR i.subcategory.id = :subcategoryId)
        AND (:cityId = 0L OR i.city.id = :cityId)
        ORDER BY i.updatedAt DESC, i.id DESC
    """,
        countQuery = """
        SELECT COUNT(i.id) FROM ItemEntity i
        JOIN i.user u
        WHERE i.state = :state
        AND u.enabled = :enabled
        AND (:categoryId = 0L OR EXISTS (
            SELECT 1 FROM i.categories c WHERE c.id = :categoryId
        ))
        AND (:subcategoryId = 0L OR i.subcategory.id = :subcategoryId)
        AND (:cityId = 0L OR i.city.id = :cityId)
    """)
    fun findActiveItemIdsByFilters(
        @Param("categoryId") categoryId: Long,
        @Param("subcategoryId") subcategoryId: Long,
        @Param("cityId") cityId: Long,
        @Param("state") state: State,
        @Param("enabled") enabled: Boolean,
        pageable: Pageable
    ): Page<UUID>

    @EntityGraph(attributePaths = [
        "user",
        "user.image",
        "categories",
        "images",
        "locations",
        "subcategory",
        "city",
        "address"
    ])
    @Query("""
        SELECT i.id FROM ItemEntity i
        WHERE i.user.id = :userId
        AND i.state = :state
        ORDER BY i.updatedAt DESC, i.id DESC
    """,
        countQuery = """
        SELECT COUNT(i.id) FROM ItemEntity i
        WHERE i.user.id = :userId
        AND i.state = :state
    """)
    fun findActiveItemIdsByUserId(
        @Param("userId") userId: UUID,
        @Param("state") state: State,
        pageable: Pageable
    ): Page<UUID>

    @EntityGraph(attributePaths = [
        "user",
        "user.image",
        "categories",
        "images",
        "locations",
        "subcategory",
        "city",
        "address"
    ])
    fun findAllByIdIn(ids: Collection<UUID>): List<ItemEntity>

    @EntityGraph(attributePaths = [
        "user",
        "user.image",
        "categories",
        "images",
        "locations",
        "subcategory",
        "city",
        "address"
    ])
    @Query("""
        SELECT DISTINCT i FROM ItemEntity i
        JOIN i.user u
        WHERE i.state = :state
        AND u.enabled = :enabled
    """)
    fun findAllActiveItemsForSearch(
        @Param("state") state: State,
        @Param("enabled") enabled: Boolean
    ): List<ItemEntity>

    @EntityGraph(attributePaths = [
        "user",
        "user.image",
        "categories",
        "images",
        "locations",
        "subcategory",
        "city",
        "address"
    ])
    @Query("""
        SELECT DISTINCT i FROM ItemEntity i
        JOIN i.user u
        LEFT JOIN i.categories c
        LEFT JOIN i.subcategory sc
        LEFT JOIN i.address a
        WHERE i.state = :state
        AND u.enabled = :enabled
        AND (:categoryId = 0L OR EXISTS (
            SELECT 1 FROM i.categories fc WHERE fc.id = :categoryId
        ))
        AND (:subcategoryId = 0L OR i.subcategory.id = :subcategoryId)
        AND (:cityId = 0L OR i.city.id = :cityId)
        AND (
            LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(i.text, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(c.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(c.description, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(sc.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(sc.description, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(i.city.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(a.name, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(COALESCE(a.description, '')) LIKE LOWER(CONCAT('%', :query, '%'))
        )
    """)
    fun findFallbackSearchCandidates(
        @Param("categoryId") categoryId: Long,
        @Param("subcategoryId") subcategoryId: Long,
        @Param("cityId") cityId: Long,
        @Param("query") query: String,
        @Param("state") state: State,
        @Param("enabled") enabled: Boolean
    ): List<ItemEntity>
}
