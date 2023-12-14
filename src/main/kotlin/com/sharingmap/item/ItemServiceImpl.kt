package com.sharingmap.item

import com.sharingmap.category.CategoryRepository
import com.sharingmap.city.CityRepository
import com.sharingmap.subcategory.SubcategoryRepository
import com.sharingmap.user.UserRepository
import org.springframework.dao.DataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.TransactionException
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.NoSuchElementException

@Service
class ItemServiceImpl(private val itemRepository: ItemRepository,
                      private val categoryRepository: CategoryRepository,
                      private val subcategoryRepository: SubcategoryRepository,
                      private val cityRepository: CityRepository,
                      private val userRepository: UserRepository
) : ItemService {

    override fun getItemById(id: UUID): ItemEntity {
        return itemRepository.findById(id).orElseThrow { NoSuchElementException("Item not found with ID: $id") }
    }

    override fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): Page<ItemEntity> {
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)

        return when {
            categoryId != 0L && subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoryIdAndSubcategoryIdAndCityId(categoryId, subcategoryId, cityId, pageable)
            categoryId != 0L && subcategoryId != 0L ->
                itemRepository.findAllByCategoryIdAndSubcategoryId(categoryId, subcategoryId, pageable)
            categoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoryIdAndCityId(categoryId, cityId, pageable)
            categoryId != 0L ->
                itemRepository.findAllByCategoryId(categoryId, pageable)
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
    override fun createItem(item: ItemEntity): ItemEntity? {
            val user = item.user?.id?.let { userRepository.getReferenceById(it) }
            val category = item.category?.id?.let { categoryRepository.getReferenceById(it) }
            val subcategory = item.subcategory?.id?.let { subcategoryRepository.getReferenceById(it) }
            val city = item.city?.id?.let { cityRepository.getReferenceById(it) }
            val newItem = ItemEntity(
                name = item.name,
                category = category,
                subcategory = subcategory,
                city = city,
                text = item.text,
                location = item.location,
                user = user
            )
        try {
            itemRepository.save(newItem)
            return newItem
        } catch (ex: DataAccessException) {
            throw RuntimeException("Error occurred while creating the item.", ex)
        } catch (ex: TransactionException) {
            throw RuntimeException("Transaction failed while creating the item.", ex)
        }
    }

    override fun deleteItem(id: UUID): Boolean {
        val existingItem = itemRepository.findById(id)
            .orElseThrow { NoSuchElementException("Item with id $id not found") }
        itemRepository.delete(existingItem)
        return true
    }

    override fun updateItem(item: ItemEntity) {
        val itemId = item.id
        itemId?.let {
            itemRepository.findById(itemId)
                .orElseThrow { NoSuchElementException("Item with id $itemId not found") }
            itemRepository.save(item)
        } ?: {
            throw NoSuchElementException("please define item id for id")
        }
    }

    override fun getAllItemsByUserId(userId: UUID, page: Int, size: Int): Page<ItemEntity> {
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)
        val items = itemRepository.findAllByUserId(userId, pageable)
        if (items.isEmpty()) {
            throw NoSuchElementException("No items found for user ID: $userId")
        }
        return items
    }

}