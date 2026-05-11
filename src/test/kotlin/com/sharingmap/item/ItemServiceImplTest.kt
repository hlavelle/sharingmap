package com.sharingmap.item

import com.sharingmap.category.CategoryService
import com.sharingmap.city.CityService
import com.sharingmap.adresses.AddressService
import com.sharingmap.location.LocationService
import com.sharingmap.search.ItemEmbeddingService
import com.sharingmap.subcategory.SubcategoryEntity
import com.sharingmap.subcategory.SubcategoryService
import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

class ItemServiceImplTest {
//
//    @Test
//    fun deletingItem() {
//        val mockItemRepository: ItemRepository = mock()
//        val mockCategoryService: CategoryService = mock()
//        val mockSubcategoryService: SubcategoryService = mock()
//        val mockCityService: CityService = mock()
//        val mockUserService: UserService = mock()
//        val mockLocationService: LocationService = mock()
//
//        val userUuid = UUID.randomUUID()
//        val itemUuid = UUID.randomUUID()
//        whenever(mockItemRepository.findById(userUuid)).thenReturn(Optional.of(item))
//        val itemService = ItemServiceImpl(mockItemRepository, mockCategoryService, mockSubcategoryService,
//            mockCityService, mockUserService, mockLocationService)
//        itemService.deleteItem(userUuid, itemUuid, true)
//        assertEquals(State.DELETED, item.state)
//    }

    private val mockItemRepository: ItemRepository = mock()
    private val mockCategoryService: CategoryService = mock()
    private val mockSubcategoryService: SubcategoryService = mock()
    private val mockCityService: CityService = mock()
    private val mockUserService: UserService = mock()
    private val mockLocationService: LocationService = mock()
    private val mockAddressService: AddressService = mock()
    private val mockItemEmbeddingService: ItemEmbeddingService = mock()

    private val itemService = ItemServiceImpl(
        mockItemRepository,
        mockCategoryService,
        mockSubcategoryService,
        mockCityService,
        mockUserService,
        mockLocationService,
        mockAddressService,
        mockItemEmbeddingService
    )

    @Test
    fun `deleteItem should delete item and update state and isGiftedOnSM on true when user owns the item`() {
        val userId = UUID.randomUUID()
        val itemId = UUID.randomUUID()
        val mockUser: UserEntity = mock()
        val item = ItemEntity(
            name = "Test Item",
            categories = mutableSetOf(),
            subcategory = mock(),
            city = mock(),
            text = "Test text",
            locations = mutableSetOf(),
            user = mockUser,
            state = State.ACTIVE
        )

        whenever(mockUser.id).thenReturn(userId)
        whenever(mockItemRepository.findById(itemId)).thenReturn(Optional.of(item))

        itemService.deleteItem(userId, itemId, true)

        assertEquals(State.DELETED, item.state)
        assertTrue(item.isGiftedOnSM)
        verify(mockItemRepository).findById(itemId)
        verify(mockItemEmbeddingService).deleteEmbedding(itemId)
    }

    @Test
    fun `deleteItem should delete item and update state and isGiftedOnSM on false when user owns the item`() {
        val userId = UUID.randomUUID()
        val itemId = UUID.randomUUID()
        val mockUser: UserEntity = mock()
        val item = ItemEntity(
            name = "Test Item",
            categories = mutableSetOf(),
            subcategory = mock(),
            city = mock(),
            text = "Test text",
            locations = mutableSetOf(),
            user = mockUser,
            state = State.ACTIVE
        )

        whenever(mockUser.id).thenReturn(userId)
        whenever(mockItemRepository.findById(itemId)).thenReturn(Optional.of(item))

        itemService.deleteItem(userId, itemId, false)

        assertEquals(State.DELETED, item.state)
        assertFalse(item.isGiftedOnSM)
        verify(mockItemRepository).findById(itemId)
        verify(mockItemEmbeddingService).deleteEmbedding(itemId)
    }

    @Test
    fun `deleteItem should throw IllegalArgumentException when user does not own the item`() {
        val userId = UUID.randomUUID()
        val otherUserId = UUID.randomUUID()
        val itemId = UUID.randomUUID()
        val mockUser:UserEntity = mock()
        val item = ItemEntity(
            name = "Test Item",
            categories = mutableSetOf(),
            subcategory = mock(),
            city = mock(),
            text = "Test text",
            locations = mutableSetOf(),
            user = mockUser,
            state = State.ACTIVE
        )

        whenever(mockUser.id).thenReturn(otherUserId)
        whenever(mockItemRepository.findById(itemId)).thenReturn(Optional.of(item))

        val exception = assertThrows<IllegalArgumentException> {
            itemService.deleteItem(userId, itemId, true)
        }

        assertEquals("This user does not have permission to delete this item.", exception.message)
        verify(mockItemRepository).findById(itemId)
    }

    @Test
    fun `deleteItem should throw NoSuchElementException when item does not exist`() {
        val userId = UUID.randomUUID()
        val itemId = UUID.randomUUID()

        whenever(mockItemRepository.findById(itemId)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> {
            itemService.deleteItem(userId, itemId, true)
        }

        assertEquals("Item not found with ID: $itemId", exception.message)
        verify(mockItemRepository).findById(itemId)
    }

    @Test
    fun `searchActiveItemsByEnabledUsers should delegate blank query to two phase filtered listing preserving order`() {
        val firstId = UUID.randomUUID()
        val secondId = UUID.randomUUID()
        val first = item(firstId)
        val second = item(secondId)
        val pageable = PageRequest.of(0, 10)
        whenever(
            mockItemRepository.findActiveItemIdsByFilters(
                categoryId = 2,
                subcategoryId = 3,
                cityId = 4,
                state = State.ACTIVE,
                enabled = true,
                pageable = pageable
            )
        ).thenReturn(PageImpl(listOf(secondId, firstId), pageable, 4))
        whenever(mockItemRepository.findAllByIdIn(listOf(secondId, firstId))).thenReturn(listOf(first, second))

        val result = itemService.searchActiveItemsByEnabledUsers(
            query = "  ",
            categoryId = 2,
            subcategoryId = 3,
            cityId = 4,
            page = 0,
            size = 10
        )

        assertEquals(listOf(second, first), result.content)
        assertEquals(4, result.totalElements)
    }

    @Test
    fun `getAllActiveItemsByUserId should use two phase pagination preserving order`() {
        val userId = UUID.randomUUID()
        val firstId = UUID.randomUUID()
        val secondId = UUID.randomUUID()
        val first = item(firstId)
        val second = item(secondId)
        val pageable = PageRequest.of(1, 20)

        whenever(mockUserService.getUserById(userId)).thenReturn(mock())
        whenever(mockItemRepository.findActiveItemIdsByUserId(userId, State.ACTIVE, pageable))
            .thenReturn(PageImpl(listOf(secondId, firstId), pageable, 5))
        whenever(mockItemRepository.findAllByIdIn(listOf(secondId, firstId))).thenReturn(listOf(first, second))

        val result = itemService.getAllActiveItemsByUserId(userId, page = 1, size = 20)

        assertEquals(listOf(second, first), result.content)
        assertEquals(5, result.totalElements)
    }

    @Test
    fun `searchActiveItemsByEnabledUsers should use embedding service for non blank query`() {
        val page = PageImpl<ItemEntity>(emptyList())
        whenever(
            mockItemEmbeddingService.semanticSearch(
                query = "велосипед",
                categoryId = 2,
                subcategoryId = 3,
                cityId = 4,
                page = 0,
                size = 10
            )
        ).thenReturn(page)

        val result = itemService.searchActiveItemsByEnabledUsers(
            query = "  велосипед  ",
            categoryId = 2,
            subcategoryId = 3,
            cityId = 4,
            page = 0,
            size = 10
        )

        assertSame(page, result)
        verify(mockItemEmbeddingService).semanticSearch(
            query = "велосипед",
            categoryId = 2,
            subcategoryId = 3,
            cityId = 4,
            page = 0,
            size = 10
        )
    }

    private fun item(id: UUID): ItemEntity {
        return ItemEntity(
            id = id,
            name = "Test Item",
            categories = mutableSetOf(),
            subcategory = mock(),
            city = mock(),
            text = "Test text",
            locations = mutableSetOf(),
            user = mock(),
            state = State.ACTIVE
        )
    }
}
