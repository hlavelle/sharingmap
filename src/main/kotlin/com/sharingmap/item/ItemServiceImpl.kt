package com.sharingmap.item

import com.sharingmap.category.CategoryService
import com.sharingmap.city.CityService
import com.sharingmap.location.LocationService
import com.sharingmap.subcategory.SubcategoryService
import com.sharingmap.user.UserNotFoundException
import com.sharingmap.user.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.NoSuchElementException

@Service
class ItemServiceImpl(private val itemRepository: ItemRepository,
                      private val categoryService: CategoryService,
                      private val subcategoryService: SubcategoryService,
                      private val cityService: CityService,
                      private val userService: UserService,
                      private val locationService: LocationService
) : ItemService {

    override fun getItemById(id: UUID): ItemEntity {
        return itemRepository.findById(id).orElseThrow { NoSuchElementException("Item not found with ID: $id") }
    }

    override fun getAllActiveItemsByEnabledUsers(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): Page<ItemEntity> {
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)
        val enabled = true

        return when {
            categoryId != 0L && subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoriesIdAndSubcategoryIdAndCityIdAndStateAndUserEnabled(categoryId, subcategoryId, cityId, State.ACTIVE, enabled, pageable)
            categoryId != 0L && subcategoryId != 0L ->
                itemRepository.findAllByCategoriesIdAndSubcategoryIdAndStateAndUserEnabled(categoryId, subcategoryId, State.ACTIVE, enabled, pageable)
            categoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoriesIdAndCityIdAndStateAndUserEnabled(categoryId, cityId, State.ACTIVE, enabled, pageable)
            categoryId != 0L ->
                itemRepository.findAllByCategoriesIdAndStateAndUserEnabled(categoryId, State.ACTIVE, enabled, pageable)
            subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllBySubcategoryIdAndCityIdAndStateAndUserEnabled(subcategoryId, cityId, State.ACTIVE, enabled, pageable)
            subcategoryId != 0L ->
                itemRepository.findAllBySubcategoryIdAndStateAndUserEnabled(subcategoryId, State.ACTIVE, enabled, pageable)
            cityId != 0L ->
                itemRepository.findAllByCityIdAndStateAndUserEnabled(cityId, State.ACTIVE, enabled, pageable)
            else ->
                itemRepository.findAllByStateAndUserEnabled(State.ACTIVE, enabled, pageable)
        }
    }

    @Transactional
    override fun createItem(userId: UUID, item: ItemCreateDto): ItemEntity? {
        val user = userService.getUserById(userId)
        val categories = item.categoriesId.map {  categoryService.getCategoryById(it) }
        val subcategory = subcategoryService.getSubcategoryById(item.subcategoryId)
        val city = cityService.getCityById(item.cityId)
        val locations = item.locationsId.map { locationService.getLocationById(it) }
        val newItem = ItemEntity(
            name = item.name,
            categories = categories.toMutableSet(),
            subcategory = subcategory,
            city = city,
            text = item.text,
            locations = locations.toMutableSet(),
            user = user,
            state = State.ACTIVE
        )
        itemRepository.save(newItem)
        return newItem
    }

    override fun adminDeleteItem(itemId: UUID) {
        itemRepository.findById(itemId).orElseThrow { NoSuchElementException("Item not found with ID: $itemId") }
        itemRepository.deleteById(itemId)
    }

    override fun deleteItem(userId: UUID, itemId: UUID, isGiftedOnSm: Boolean) {
        val item = itemRepository.findById(itemId).orElseThrow { NoSuchElementException("Item not found with ID: $itemId") }
        if (userId != item.user?.id) {
            throw IllegalArgumentException("This user does not have permission to delete this item.")
        }
        item.state = State.DELETED
        item.isGiftedOnSM = isGiftedOnSm
        item.user?.let { user ->
            user.giftedItems = (user.giftedItems ?: 0) + 1
        }
        itemRepository.save(item)
    }

    override fun adminUpdateItem(item: ItemUpdateDto) {
        val newItem = itemRepository.findById(item.id)
            .orElseThrow { java.util.NoSuchElementException("Item not found with ID: ${item.id}") }

        if (item.name != null) newItem.name = item.name
        if (item.text != null) newItem.text = item.text
        if (!item.categoriesId.isNullOrEmpty()) {
            val categories = item.categoriesId.map { categoryService.getCategoryById(it) }
            newItem.categories = categories.toMutableSet()
        }
        if (item.cityId != null) {
            val city = cityService.getCityById(item.cityId)
            newItem.city = city
        }
        if (!item.locationsId.isNullOrEmpty()) {
            val locations = item.locationsId.map { locationService.getLocationById(it) }
            newItem.locations = locations.toMutableSet()
        }
        itemRepository.save(newItem)
    }

    override fun updateItem(userId: UUID, item: ItemUpdateDto) {
        val newItem = itemRepository.findById(item.id)
            .orElseThrow { java.util.NoSuchElementException("Item not found with ID: ${item.id}") }
        if (userId != newItem.user?.id) {
            throw IllegalArgumentException("This user does not have permission to update this contact.")
        }
        if (item.name != null) newItem.name = item.name
        if (item.text != null) newItem.text = item.text
        if (!item.categoriesId.isNullOrEmpty()) {
            val categories = item.categoriesId.map { categoryService.getCategoryById(it) }
            newItem.categories = categories.toMutableSet()
        }
        if (item.cityId != null) {
            val city = cityService.getCityById(item.cityId)
            newItem.city = city
        }
        if (!item.locationsId.isNullOrEmpty()) {
            val locations = item.locationsId.map { locationService.getLocationById(it) }
            newItem.locations = locations.toMutableSet()
        }
        itemRepository.save(newItem)
    }

    override fun getAllActiveItemsByUserId(userId: UUID, page: Int, size: Int): Page<ItemEntity> {
        userService.getUserById(userId)
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)
        val items = itemRepository.findAllByUserIdAndState(userId, State.ACTIVE, pageable)
        if (items.isEmpty) {
            throw NoSuchElementException("No items found for user ID: $userId")
        }
        return items
    }

}