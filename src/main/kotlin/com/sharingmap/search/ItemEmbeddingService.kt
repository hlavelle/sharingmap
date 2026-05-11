package com.sharingmap.search

import com.sharingmap.item.ItemEntity
import com.sharingmap.item.ItemRepository
import com.sharingmap.item.State
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.math.min

@Service
class ItemEmbeddingService(
    private val itemRepository: ItemRepository,
    private val searchTextBuilder: ItemSearchTextBuilder,
    private val embeddingProvider: EmbeddingProvider,
    private val vectorRepository: PgVectorItemEmbeddingRepository,
    private val properties: SemanticSearchProperties
) {
    private val logger = LoggerFactory.getLogger(ItemEmbeddingService::class.java)

    fun upsertEmbedding(item: ItemEntity): Boolean {
        if (!properties.enabled || item.state != State.ACTIVE) {
            return false
        }
        return runCatching {
            if (!vectorRepository.initializeIfEnabled()) {
                return false
            }
            val searchText = searchTextBuilder.build(item)
            if (searchText.isBlank()) {
                return false
            }
            val embedding = embeddingProvider.embedDocument(searchText)
            validateEmbedding(embedding)
            vectorRepository.upsert(item.id, searchText, embedding, embeddingProvider.modelName)
            true
        }.getOrElse { ex ->
            logger.warn("Could not upsert embedding for item {}: {}", item.id, ex.message)
            false
        }
    }

    fun deleteEmbedding(itemId: UUID) {
        runCatching { vectorRepository.delete(itemId) }
            .onFailure { ex -> logger.warn("Could not delete embedding for item {}: {}", itemId, ex.message) }
    }

    fun reindexAllActiveItems(): ReindexResult {
        if (!properties.enabled) {
            return ReindexResult(processed = 0, failed = 0)
        }
        var processed = 0
        var failed = 0
        itemRepository.findAllActiveItemsForSearch(State.ACTIVE, true).forEach { item ->
            if (upsertEmbedding(item)) {
                processed += 1
            } else {
                failed += 1
            }
        }
        return ReindexResult(processed = processed, failed = failed)
    }

    fun semanticSearch(
        query: String,
        categoryId: Long,
        subcategoryId: Long,
        cityId: Long,
        page: Int,
        size: Int
    ): Page<ItemEntity> {
        if (!properties.enabled || query.isBlank()) {
            return fallbackLexicalSearch(query, categoryId, subcategoryId, cityId, page, size)
        }
        return runCatching {
            if (!vectorRepository.initializeIfEnabled()) {
                return fallbackLexicalSearch(query, categoryId, subcategoryId, cityId, page, size)
            }
            val queryEmbedding = embeddingProvider.embedQuery(query)
            validateEmbedding(queryEmbedding)
            val pageable = PageRequest.of(page, size)
            val ids = vectorRepository.findIdsByVector(
                queryEmbedding = queryEmbedding,
                categoryId = categoryId,
                subcategoryId = subcategoryId,
                cityId = cityId,
                maxDistance = properties.maxDistance,
                limit = size,
                offset = pageable.offset
            )
            val items = preserveOrder(ids, itemRepository.findAllByIdIn(ids))
            PageImpl(
                items,
                pageable,
                vectorRepository.countByVector(queryEmbedding, categoryId, subcategoryId, cityId, properties.maxDistance)
            )
        }.getOrElse { ex ->
            logger.warn("Semantic search failed; using fallback lexical search: {}", ex.message)
            fallbackLexicalSearch(query, categoryId, subcategoryId, cityId, page, size)
        }
    }

    fun fallbackLexicalSearch(
        query: String,
        categoryId: Long,
        subcategoryId: Long,
        cityId: Long,
        page: Int,
        size: Int
    ): Page<ItemEntity> {
        val pageable = PageRequest.of(page, size)
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isBlank()) {
            val idsPage = itemRepository.findActiveItemIdsByFilters(
                categoryId = categoryId,
                subcategoryId = subcategoryId,
                cityId = cityId,
                state = State.ACTIVE,
                enabled = true,
                pageable = pageable
            )
            return PageImpl(
                preserveOrder(idsPage.content, itemRepository.findAllByIdIn(idsPage.content)),
                pageable,
                idsPage.totalElements
            )
        }
        val candidates = itemRepository.findFallbackSearchCandidates(
            categoryId = categoryId,
            subcategoryId = subcategoryId,
            cityId = cityId,
            query = normalizedQuery,
            state = State.ACTIVE,
            enabled = true
        )
        val ranked = candidates.sortedWith(
            compareBy<ItemEntity> { levenshteinScore(normalizedQuery, searchTextBuilder.build(it).lowercase()) }
                .thenByDescending { it.updatedAt }
        )
        val from = min(pageable.offset.toInt(), ranked.size)
        val to = min(from + pageable.pageSize, ranked.size)
        return PageImpl(ranked.subList(from, to), pageable, ranked.size.toLong())
    }

    private fun preserveOrder(ids: List<UUID>, items: List<ItemEntity>): List<ItemEntity> {
        val byId = items.associateBy { it.id }
        return ids.mapNotNull { byId[it] }
    }

    private fun validateEmbedding(embedding: List<Float>) {
        if (embedding.isEmpty()) {
            throw IllegalStateException("Embedding provider returned empty vector")
        }
        if (embedding.size != properties.embeddingDim) {
            throw IllegalStateException("Embedding dimension ${embedding.size} does not match configured ${properties.embeddingDim}")
        }
    }

    private fun levenshteinScore(query: String, text: String): Int {
        if (text.contains(query)) {
            return 0
        }
        val words = text.split(' ').filter { it.isNotBlank() }
        if (words.isEmpty()) {
            return query.length
        }
        return words.minOf { levenshtein(query, it) }
    }

    private fun levenshtein(left: String, right: String): Int {
        if (left == right) return 0
        if (left.isEmpty()) return right.length
        if (right.isEmpty()) return left.length

        var previous = IntArray(right.length + 1) { it }
        var current = IntArray(right.length + 1)
        for (i in left.indices) {
            current[0] = i + 1
            for (j in right.indices) {
                val cost = if (left[i] == right[j]) 0 else 1
                current[j + 1] = minOf(
                    current[j] + 1,
                    previous[j + 1] + 1,
                    previous[j] + cost
                )
            }
            val tmp = previous
            previous = current
            current = tmp
        }
        return previous[right.length]
    }
}

data class ReindexResult(
    val processed: Int,
    val failed: Int
)
