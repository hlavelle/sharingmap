package com.sharingmap.controllers

import com.sharingmap.entities.ItemEntity
import com.sharingmap.services.ItemService
import org.springframework.web.bind.annotation.*

@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/items/{id}")
    fun getItemById(@PathVariable id: Long): ItemEntity {
        return itemService.getItemById(id)
    }

    @GetMapping("{categoryId}/{cityId}/items") //TODO поиск по субкатегориям
    fun getAllItems(@PathVariable(value = "categoryId") categoryId: Long,
                    @PathVariable(value = "cityId") cityId: Long): List<ItemEntity> {
        return itemService.getAllItems(categoryId, cityId)
    }

    @PostMapping("/users/{userId}/{categoryId}/{cityId}/items")
    fun createItem(@PathVariable(value = "userId") userId: Long,
                   @PathVariable(value = "categoryId") categoryId: Long,
                   @PathVariable(value = "cityId") cityId: Long,
                   @RequestBody item: ItemEntity) {
        itemService.createItem(userId, categoryId, cityId, item)
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: Long) {
        itemService.deleteItem(id)
    }

    @PutMapping("/items/{id}")
    fun updateItem(@PathVariable id: Long, @RequestBody item: ItemEntity) {
        itemService.updateItem(id, item)
    }

    @GetMapping("/users/{userId}/items")
    fun getAllItemsByUserId(@PathVariable(value = "userId") userId: Long): List<ItemEntity> {
        return itemService.getAllItemsByUserId(userId)
    }
}