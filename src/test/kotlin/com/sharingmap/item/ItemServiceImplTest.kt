package com.sharingmap.item

import com.sharingmap.category.CategoryService
import com.sharingmap.city.CityService
import com.sharingmap.location.LocationService
import com.sharingmap.subcategory.SubcategoryEntity
import com.sharingmap.subcategory.SubcategoryService
import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserService
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

    private val itemService = ItemServiceImpl(
        mockItemRepository,
        mockCategoryService,
        mockSubcategoryService,
        mockCityService,
        mockUserService,
        mockLocationService
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
}