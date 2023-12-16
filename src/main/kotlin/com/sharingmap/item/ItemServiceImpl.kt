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

    override fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): Page<ItemEntity> {
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)

        return when {
            categoryId != 0L && subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoriesIdAndSubcategoryIdAndCityId(categoryId, subcategoryId, cityId, pageable)
            categoryId != 0L && subcategoryId != 0L ->
                itemRepository.findAllByCategoriesIdAndSubcategoryId(categoryId, subcategoryId, pageable)
            categoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoriesIdAndCityId(categoryId, cityId, pageable)
            categoryId != 0L ->
                itemRepository.findAllByCategoriesId(categoryId, pageable)
            subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllBySubcategoryIdAndCityId(subcategoryId, cityId, pageable)
            subcategoryId != 0L ->
                itemRepository.findAllBySubcategoryId(subcategoryId, pageable)
            cityId != 0L ->
                itemRepository.findAllByCityId(cityId, pageable)
            else ->
                itemRepository.findAll(pageable)
        }
    }

    @Transactional
    override fun createItem(id: UUID, item: ItemCreateDto): ItemEntity? {
        val user = userService.getUserById(id)
        val categories = item.categoriesId.map {  categoryService.getCategoryById(it) }
        val subcategory = subcategoryService.getSubcategoryById(item.subcategoryId)
        val city = cityService.getCityById(item.cityId)
        val location = locationService.getLocationById(item.locationId)
        val newItem = ItemEntity(
                name = item.name,
                categories = categories.toMutableSet(),
                subcategory = subcategory,
                city = city,
                text = item.text,
                location = location,
                user = user
            )
        itemRepository.save(newItem)
        return newItem
    }

    override fun deleteItem(id: UUID) {
        itemRepository.findById(id).orElseThrow { NoSuchElementException("Item not found with ID: $id") }
        itemRepository.deleteById(id)
    }

    override fun updateItem(itemId: UUID, item: ItemUpdateDto) {
        val newItem = itemRepository.findById(itemId)
            .orElseThrow { java.util.NoSuchElementException("Item not found with ID: $itemId") }
        if (item.name != null) newItem.name = item.name
        if (item.text != null) newItem.text = item.text
        if (item.categoriesId != null) {
            val categories = item.categoriesId.map { categoryService.getCategoryById(it) }
            newItem.categories = categories.toMutableSet()
        }
        if (item.cityId != null) {
            val city = cityService.getCityById(item.cityId)
            newItem.city = city
        }
        if (item.locationId != null) {
            val location = locationService.getLocationById(item.locationId)
            newItem.location = location
        }
    }

    override fun getAllItemsByUserId(userId: UUID, page: Int, size: Int): Page<ItemEntity> {
        userService.getUserById(userId)
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)
        val items = itemRepository.findAllByUserId(userId, pageable)
        if (items.isEmpty) {
            throw NoSuchElementException("No items found for user ID: $userId")
        }
        return items
    }

}