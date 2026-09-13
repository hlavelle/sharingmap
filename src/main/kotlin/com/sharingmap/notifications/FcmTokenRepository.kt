package com.sharingmap.notifications

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FcmTokenRepository : JpaRepository<FcmTokenEntity, Long> {

    fun findAllByUserId(userId: UUID): List<FcmTokenEntity>

    fun findByToken(token: String): FcmTokenEntity?

    @Modifying
    @Query("DELETE FROM FcmTokenEntity t WHERE t.token = :token")
    fun deleteByToken(@Param("token") token: String)

    /**
     * Deletes a token only if it belongs to [userId].
     *
     * Scoped in the query rather than read-then-check so a caller cannot learn whether
     * someone else's token exists, and so there is no window between the check and the
     * delete. See known-issues #4.
     */
    @Modifying
    @Query("DELETE FROM FcmTokenEntity t WHERE t.token = :token AND t.userId = :userId")
    fun deleteByTokenAndUserId(@Param("token") token: String, @Param("userId") userId: UUID)

    @Modifying
    @Query("DELETE FROM FcmTokenEntity t WHERE t.userId = :userId")
    fun deleteAllByUserId(@Param("userId") userId: UUID)

    @Modifying
    @Query("DELETE FROM FcmTokenEntity t WHERE t.token IN :tokens")
    fun deleteAllByTokenIn(@Param("tokens") tokens: Collection<String>)

    @Query("SELECT t FROM FcmTokenEntity t WHERE t.userId IN :userIds")
    fun findAllByUserIdIn(@Param("userIds") userIds: Collection<UUID>): List<FcmTokenEntity>
}