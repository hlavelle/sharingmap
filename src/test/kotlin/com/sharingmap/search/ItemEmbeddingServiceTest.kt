package com.sharingmap.search

import com.sharingmap.city.CityEntity
import com.sharingmap.item.ItemEntity
import com.sharingmap.item.ItemRepository
import com.sharingmap.item.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.UUID

class ItemEmbeddingServiceTest {
    private val itemRepository: ItemRepository = mock()
    private val searchTextBuilder = ItemSearchTextBuilder()
    private val embeddingProvider: EmbeddingProvider = mock()
    private val vectorRepository: PgVectorItemEmbeddingRepository = mock()

    @Test
    fun `semanticSearch should use fallback when semantic search is disabled`() {
        val properties = SemanticSearchProperties().apply { enabled = false }
        val service = service(properties)
        val item = item(name = "Велосипед", text = "детский велосипед")

        whenever(
            itemRepository.findFallbackSearchCandidates(
                categoryId = 2,
                subcategoryId = 3,
                cityId = 4,
                query = "велосипед",
                state = State.ACTIVE,
                enabled = true
            )
        ).thenReturn(listOf(item))

        val result = service.semanticSearch(
            query = "велосипед",
            categoryId = 2,
            subcategoryId = 3,
            cityId = 4,
            page = 0,
            size = 10
        )

        assertEquals(listOf(item), result.content)
        verify(embeddingProvider, never()).embedQuery(any())
    }

    @Test
    fun `semanticSearch should use vector repository and preserve vector order`() {
        val properties = SemanticSearchProperties().apply { enabled = true }
        val service = service(properties)
        val first = item(name = "Коляска", text = "для прогулок")
        val second = item(name = "Велосипед", text = "для ребенка")
        val orderedIds = listOf(second.id, first.id)

        whenever(vectorRepository.initializeIfEnabled()).thenReturn(true)
        whenever(embeddingProvider.embedQuery("детский транспорт")).thenReturn(List(256) { 0.1f })
        whenever(
            vectorRepository.findIdsByVector(
                queryEmbedding = List(256) { 0.1f },
                categoryId = 2,
                subcategoryId = 1,
                cityId = 4,
                maxDistance = properties.maxDistance,
                limit = 10,
                offset = 0L
            )
        ).thenReturn(orderedIds)
        whenever(itemRepository.findAllByIdIn(orderedIds)).thenReturn(listOf(first, second))
        whenever(
            vectorRepository.countByVector(
                queryEmbedding = List(256) { 0.1f },
                categoryId = 2,
                subcategoryId = 1,
                cityId = 4,
                maxDistance = properties.maxDistance
            )
        ).thenReturn(2L)

        val result = service.semanticSearch(
            query = "детский транспорт",
            categoryId = 2,
            subcategoryId = 1,
            cityId = 4,
            page = 0,
            size = 10
        )

        assertEquals(listOf(second, first), result.content)
        verify(embeddingProvider).embedQuery("детский транспорт")
    }

    @Test
    fun `semanticSearch should use fallback when embedding provider fails`() {
        val properties = SemanticSearchProperties().apply { enabled = true }
        val service = service(properties)
        val item = item(name = "Тостер", text = "кухонный прибор")

        whenever(vectorRepository.initializeIfEnabled()).thenReturn(true)
        whenever(embeddingProvider.embedQuery("тостер")).thenThrow(IllegalStateException("provider error"))
        whenever(
            itemRepository.findFallbackSearchCandidates(
                categoryId = 0,
                subcategoryId = 1,
                cityId = 1,
                query = "тостер",
                state = State.ACTIVE,
                enabled = true
            )
        ).thenReturn(listOf(item))

        val result = service.semanticSearch(
            query = "тостер",
            categoryId = 0,
            subcategoryId = 1,
            cityId = 1,
            page = 0,
            size = 10
        )

        assertEquals(listOf(item), result.content)
    }

    @Test
    fun `semanticSearch should use fallback when vector storage is unavailable`() {
        val properties = SemanticSearchProperties().apply { enabled = true }
        val service = service(properties)
        val item = item(name = "Книга", text = "программирование")

        whenever(vectorRepository.initializeIfEnabled()).thenReturn(false)
        whenever(
            itemRepository.findFallbackSearchCandidates(
                categoryId = 4,
                subcategoryId = 1,
                cityId = 1,
                query = "книга",
                state = State.ACTIVE,
                enabled = true
            )
        ).thenReturn(listOf(item))

        val result = service.semanticSearch(
            query = "книга",
            categoryId = 4,
            subcategoryId = 1,
            cityId = 1,
            page = 0,
            size = 10
        )

        assertEquals(listOf(item), result.content)
        verify(embeddingProvider, never()).embedQuery(any())
    }

    @Test
    fun `fallbackLexicalSearch should delegate blank query to filtered listing with pagination`() {
        val properties = SemanticSearchProperties().apply { enabled = false }
        val service = service(properties)
        val pageable = PageRequest.of(1, 20)
        val first = item(name = "Первый", text = "текст")
        val second = item(name = "Второй", text = "текст")

        whenever(
            itemRepository.findActiveItemIdsByFilters(
                categoryId = 2,
                subcategoryId = 3,
                cityId = 4,
                state = State.ACTIVE,
                enabled = true,
                pageable = pageable
            )
        ).thenReturn(PageImpl(listOf(second.id, first.id), pageable, 5))
        whenever(itemRepository.findAllByIdIn(listOf(second.id, first.id))).thenReturn(listOf(first, second))

        val result = service.fallbackLexicalSearch(
            query = " ",
            categoryId = 2,
            subcategoryId = 3,
            cityId = 4,
            page = 1,
            size = 20
        )

        assertEquals(listOf(second, first), result.content)
        assertEquals(5, result.totalElements)
    }

    private fun service(properties: SemanticSearchProperties): ItemEmbeddingService {
        return ItemEmbeddingService(
            itemRepository = itemRepository,
            searchTextBuilder = searchTextBuilder,
            embeddingProvider = embeddingProvider,
            vectorRepository = vectorRepository,
            properties = properties
        )
    }

    private fun item(name: String, text: String): ItemEntity {
        return ItemEntity(
            id = UUID.randomUUID(),
            name = name,
            text = text,
            categories = emptySet(),
            subcategory = null,
            city = CityEntity().apply { this.name = "Москва" },
            locations = emptySet(),
            user = null,
            state = State.ACTIVE
        )
    }
}
