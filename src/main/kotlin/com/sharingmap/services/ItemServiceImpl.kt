package com.sharingmap.services

import com.sharingmap.entities.ItemEntity
import com.sharingmap.repositories.*
import org.springframework.dao.DataAccessException
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
                      private val userRepository: UserRepository) : ItemService {

    override fun getItemById(id: UUID): ItemEntity {
        return itemRepository.findById(id).orElseThrow { NoSuchElementException("Item not found with ID: $id") }
    }

    override fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long, page: Int, size: Int): List<ItemEntity> {
        val sort = Sort.by(Sort.Direction.DESC, "updatedAt")
        val pageable = PageRequest.of(page, size, sort)

        return when {
            categoryId != 0L && subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoryIdAndSubcategoryIdAndCityId(categoryId, subcategoryId, cityId, pageable).toList()
            categoryId != 0L && subcategoryId != 0L ->
                itemRepository.findAllByCategoryIdAndSubcategoryId(categoryId, subcategoryId, pageable).toList()
            categoryId != 0L && cityId != 0L ->
                itemRepository.findAllByCategoryIdAndCityId(categoryId, cityId, pageable).toList()
            categoryId != 0L ->
                itemRepository.findAllByCategoryId(categoryId, pageable).toList()
            subcategoryId != 0L && cityId != 0L ->
                itemRepository.findAllBySubcategoryIdAndCityId(subcategoryId, cityId, pageable).toList()
            subcategoryId != 0L ->
                itemRepository.findAllBySubcategoryId(subcategoryId, pageable).toList()
            cityId != 0L ->
                itemRepository.findAllByCityId(cityId, pageable).toList()
            else ->
                itemRepository.findAll(pageable).toList()
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
                address = item.address,
                phoneNumber = item.phoneNumber,
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

    override fun getAllItemsByUserId(userId: UUID): List<ItemEntity> {
        val items = itemRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "updatedAt"))
        if (items.isEmpty()) {
            throw NoSuchElementException("No items found for user ID: $userId")
        }
        return items
    }

}