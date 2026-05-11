package com.sharingmap.search

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

@Repository
class PgVectorItemEmbeddingRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val properties: SemanticSearchProperties
) {
    private val logger = LoggerFactory.getLogger(PgVectorItemEmbeddingRepository::class.java)

    @Volatile
    private var initialized: Boolean = false

    fun initializeIfEnabled(): Boolean {
        if (!properties.enabled) {
            return false
        }
        if (initialized) {
            return true
        }
        return try {
            val dim = properties.embeddingDim.coerceAtLeast(1)
            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector")
            jdbcTemplate.execute(
                """
                CREATE TABLE IF NOT EXISTS item_embeddings (
                    item_id uuid primary key references items(id) on delete cascade,
                    search_text text not null,
                    embedding vector($dim) not null,
                    embedding_model varchar(128) not null,
                    updated_at timestamp not null default now()
                )
                """.trimIndent()
            )
            createVectorIndex()
            initialized = true
            true
        } catch (ex: Exception) {
            logger.warn("pgvector storage is unavailable; semantic search will use fallback: {}", ex.message)
            initialized = false
            false
        }
    }

    fun isAvailable(): Boolean = initialized

    fun upsert(itemId: UUID, searchText: String, embedding: List<Float>, model: String) {
        ensureAvailable()
        val vectorLiteral = toVectorLiteral(embedding)
        jdbcTemplate.update(
            """
            INSERT INTO item_embeddings (item_id, search_text, embedding, embedding_model, updated_at)
            VALUES (?, ?, CAST(? AS vector), ?, ?)
            ON CONFLICT (item_id) DO UPDATE SET
                search_text = EXCLUDED.search_text,
                embedding = EXCLUDED.embedding,
                embedding_model = EXCLUDED.embedding_model,
                updated_at = EXCLUDED.updated_at
            """.trimIndent(),
            itemId,
            searchText,
            vectorLiteral,
            model.take(128),
            Timestamp.valueOf(LocalDateTime.now())
        )
    }

    fun delete(itemId: UUID) {
        if (!initialized) {
            return
        }
        jdbcTemplate.update("DELETE FROM item_embeddings WHERE item_id = ?", itemId)
    }

    fun findIdsByVector(
        queryEmbedding: List<Float>,
        categoryId: Long,
        subcategoryId: Long,
        cityId: Long,
        maxDistance: Double,
        limit: Int,
        offset: Long
    ): List<UUID> {
        ensureAvailable()
        val vectorLiteral = toVectorLiteral(queryEmbedding)
        return jdbcTemplate.queryForList(
            """
            SELECT i.id
            FROM items i
            JOIN item_embeddings e ON e.item_id = i.id
            JOIN users u ON u.id = i.user_id
            WHERE i.state = 'ACTIVE'
              AND u.enabled = true
              AND (? = 0 OR EXISTS (
                  SELECT 1 FROM item_category ic
                  WHERE ic.item_id = i.id AND ic.category_id = ?
              ))
              AND (? = 0 OR i.subcategory_id = ?)
              AND (? = 0 OR i.city_id = ?)
              AND (e.embedding <=> CAST(? AS vector)) <= ?
            ORDER BY e.embedding <=> CAST(? AS vector), i.updated_at DESC
            LIMIT ? OFFSET ?
            """.trimIndent(),
            UUID::class.java,
            categoryId,
            categoryId,
            subcategoryId,
            subcategoryId,
            cityId,
            cityId,
            vectorLiteral,
            maxDistance,
            vectorLiteral,
            limit,
            offset
        )
    }

    fun countByVector(
        queryEmbedding: List<Float>,
        categoryId: Long,
        subcategoryId: Long,
        cityId: Long,
        maxDistance: Double
    ): Long {
        ensureAvailable()
        val vectorLiteral = toVectorLiteral(queryEmbedding)
        return jdbcTemplate.queryForObject(
            """
            SELECT COUNT(DISTINCT i.id)
            FROM items i
            JOIN item_embeddings e ON e.item_id = i.id
            JOIN users u ON u.id = i.user_id
            WHERE i.state = 'ACTIVE'
              AND u.enabled = true
              AND (? = 0 OR EXISTS (
                  SELECT 1 FROM item_category ic
                  WHERE ic.item_id = i.id AND ic.category_id = ?
              ))
              AND (? = 0 OR i.subcategory_id = ?)
              AND (? = 0 OR i.city_id = ?)
              AND (e.embedding <=> CAST(? AS vector)) <= ?
            """.trimIndent(),
            Long::class.java,
            categoryId,
            categoryId,
            subcategoryId,
            subcategoryId,
            cityId,
            cityId,
            vectorLiteral,
            maxDistance
        ) ?: 0L
    }

    private fun createVectorIndex() {
        try {
            jdbcTemplate.execute(
                """
                CREATE INDEX IF NOT EXISTS item_embeddings_embedding_hnsw_idx
                ON item_embeddings USING hnsw (embedding vector_cosine_ops)
                """.trimIndent()
            )
        } catch (hnswEx: Exception) {
            logger.warn("Could not create pgvector hnsw index: {}", hnswEx.message)
            try {
                jdbcTemplate.execute(
                    """
                    CREATE INDEX IF NOT EXISTS item_embeddings_embedding_ivfflat_idx
                    ON item_embeddings USING ivfflat (embedding vector_cosine_ops)
                    """.trimIndent()
                )
            } catch (ivfflatEx: Exception) {
                logger.warn("Could not create pgvector ivfflat index: {}", ivfflatEx.message)
            }
        }
    }

    private fun ensureAvailable() {
        if (!initialized && !initializeIfEnabled()) {
            throw IllegalStateException("pgvector storage is unavailable")
        }
    }

    private fun toVectorLiteral(values: List<Float>): String {
        return values.joinToString(prefix = "[", postfix = "]", separator = ",") { it.toString() }
    }
}
