package com.sharingmap.search

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmbeddingProviderConfig {
    private val logger = LoggerFactory.getLogger(EmbeddingProviderConfig::class.java)

    @Bean
    fun embeddingProvider(
        semanticSearchProperties: SemanticSearchProperties,
        yandexAiProperties: YandexAiProperties
    ): EmbeddingProvider {
        if (!semanticSearchProperties.enabled) {
            return DisabledEmbeddingProvider()
        }
        if (!semanticSearchProperties.provider.equals("yandex", ignoreCase = true)) {
            logger.warn("Unsupported semantic search provider '{}'; semantic search will use fallback", semanticSearchProperties.provider)
            return DisabledEmbeddingProvider("unsupported:${semanticSearchProperties.provider}")
        }
        if (yandexAiProperties.apiKey.isBlank() && yandexAiProperties.iamToken.isBlank()) {
            logger.warn("Yandex AI credentials are not configured; semantic search will use fallback")
            return DisabledEmbeddingProvider("yandex:missing-credentials")
        }
        return YandexEmbeddingProvider(yandexAiProperties)
    }
}
