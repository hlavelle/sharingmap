package com.sharingmap.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class YandexEmbeddingProvider(
    private val properties: YandexAiProperties,
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val httpClient: HttpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build()
) : EmbeddingProvider {
    override val modelName: String = "yandex:${properties.docModelUri}:${properties.queryModelUri}"

    override fun embedDocument(text: String): List<Float> {
        return embed(properties.docModelUri, text)
    }

    override fun embedQuery(text: String): List<Float> {
        return embed(properties.queryModelUri, text)
    }

    private fun embed(modelUri: String, text: String): List<Float> {
        require(modelUri.isNotBlank()) { "Yandex modelUri is not configured" }
        val body = objectMapper.writeValueAsString(
            mapOf(
                "modelUri" to modelUri,
                "text" to text
            )
        )
        val requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("https://llm.api.cloud.yandex.net/foundationModels/v1/textEmbedding"))
            .version(HttpClient.Version.HTTP_1_1)
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))

        when {
            properties.apiKey.isNotBlank() -> requestBuilder.header("Authorization", "Api-Key ${properties.apiKey}")
            properties.iamToken.isNotBlank() -> requestBuilder.header("Authorization", "Bearer ${properties.iamToken}")
            else -> throw IllegalStateException("Yandex AI credentials are not configured")
        }
        val response = sendWithRetry(requestBuilder.build())
        if (response.statusCode() !in 200..299) {
            throw IllegalStateException("Yandex embedding request failed with status ${response.statusCode()}")
        }
        val parsed: YandexEmbeddingResponse = objectMapper.readValue(response.body())
        return parsed.embedding
    }

    private fun sendWithRetry(request: HttpRequest): HttpResponse<String> {
        var lastException: IOException? = null
        repeat(2) { attempt ->
            try {
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
                throw ex
            } catch (ex: IOException) {
                lastException = ex
                if (attempt == 1) {
                    throw ex
                }
            }
        }
        throw lastException ?: IOException("Yandex embedding request failed")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class YandexEmbeddingResponse(
        val embedding: List<Float> = emptyList()
    )
}
