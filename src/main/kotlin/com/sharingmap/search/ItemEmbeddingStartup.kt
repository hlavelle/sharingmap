package com.sharingmap.search

import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ItemEmbeddingStartup(
    private val properties: SemanticSearchProperties,
    private val vectorRepository: PgVectorItemEmbeddingRepository,
    private val itemEmbeddingService: ItemEmbeddingService
) {
    private val logger = LoggerFactory.getLogger(ItemEmbeddingStartup::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        if (!properties.enabled) {
            return
        }
        if (!vectorRepository.initializeIfEnabled()) {
            return
        }
        if (properties.reindexOnStartup) {
            val result = itemEmbeddingService.reindexAllActiveItems()
            logger.info("Semantic search reindex finished: processed={}, failed={}", result.processed, result.failed)
        }
    }
}
