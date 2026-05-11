package com.sharingmap.search

interface EmbeddingProvider {
    val modelName: String
    fun embedDocument(text: String): List<Float>
    fun embedQuery(text: String): List<Float>
}

class DisabledEmbeddingProvider(
    override val modelName: String = "disabled"
) : EmbeddingProvider {
    override fun embedDocument(text: String): List<Float> {
        throw IllegalStateException("Semantic search embedding provider is disabled")
    }

    override fun embedQuery(text: String): List<Float> {
        throw IllegalStateException("Semantic search embedding provider is disabled")
    }
}
