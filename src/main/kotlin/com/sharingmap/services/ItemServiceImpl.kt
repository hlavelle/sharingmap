package com.sharingmap.services

import com.sharingmap.entities.ItemEntity
import com.sharingmap.repositories.*
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ItemServiceImpl(private val itemRepository: ItemRepository,
                      private val categoryRepository: CategoryRepository,
                      private val subcategoryRepository: SubcategoryRepository,
                      private val cityRepository: CityRepository,
                      private val userRepository: UserRepository) : ItemService {

    override fun getItemById(id: Long): ItemEntity = itemRepository.findById(id).get()

    override fun getAllItems(categoryId: Long, subcategoryId: Long, cityId: Long): List<ItemEntity> { //TODO доделать фильтрацию по субкатегориям
        return if (categoryId != 0L && cityId != 0L) {
            itemRepository.findAllByCategoryIdAndCityId(categoryId, cityId, Sort.by(Sort.Direction.DESC, "updatedAt")).toList()
        } else if (categoryId != 0L) {
            itemRepository.findAllByCategoryId(categoryId, Sort.by(Sort.Direction.DESC, "updatedAt")).toList()
        } else if (cityId != 0L){
            itemRepository.findAllByCityId(cityId, Sort.by(Sort.Direction.DESC, "updatedAt")).toList()
        } else {
            itemRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")).toList()
        }
    }

    override fun createItem(userId: Long, categoryId: Long, subcategoryId: Long, cityId: Long, item: ItemEntity) {
        item.user = userRepository.findById(userId).get()
        item.category = categoryRepository.findById(categoryId).get()
        item.subcategory = subcategoryRepository.findById(subcategoryId).get()
        item.city = cityRepository.findById(cityId).get()
        itemRepository.save(item)
        // todo on bad serialize 400
    }

    override fun deleteItem(id: Long) {
        itemRepository.deleteById(id)
    }

    override fun updateItem(id: Long, item: ItemEntity) {
        val newItem = itemRepository.findById(id).get()
        newItem.name = item.name
        //newItem.photo = item.photo
        //newItem.text = item.text
        //newItem.category = item.category
        //newItem.address = item.address
        itemRepository.save(newItem)
    }

    override fun getAllItemsByUserId(userId: Long): List<ItemEntity> = itemRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "updatedAt"))

//    fun <T : Any> Optional<out T>.toList(): List<T> =
//        if (isPresent) listOf(get()) else emptyList()
}