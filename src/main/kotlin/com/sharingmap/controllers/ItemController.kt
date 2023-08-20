package com.sharingmap.controllers

import com.sharingmap.entities.ItemEntity
import com.sharingmap.services.ItemService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/items/{id}")
    fun getItemById(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val item = itemService.getItemById(id)
            ResponseEntity.ok(item)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        }
    }


    @GetMapping("get/items")
    fun getAllItems(@RequestParam(value = "categoryId") categoryId: Long,
                    @RequestParam(value = "cityId") cityId: Long,
                    @RequestParam(value = "subcategoryId") subcategoryId: Long,
                    @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                    @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<List<ItemEntity>> {
        val items =  itemService.getAllItems(categoryId, subcategoryId, cityId, page, size)
        return ResponseEntity.ok(items)
    }

    @PostMapping("/items/create")
    fun createItem(@RequestBody @Valid item: ItemEntity): ResponseEntity<Unit>  {
        itemService.createItem(item)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/items/delete/{id}")
    fun deleteItem(@PathVariable @Min(1) id: Long): ResponseEntity<Unit> {
        val isDeleted = itemService.deleteItem(id)

        return if (isDeleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/items/update/{id}")
    fun updateItem(@PathVariable @Min(1) id: Long, @RequestBody item: ItemEntity): ResponseEntity<Unit> {
        itemService.updateItem(id, item)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/users/{userId}/items")
    fun getAllItemsByUserId(@PathVariable(value = "userId") @Min(1) userId: Long):  ResponseEntity<Any> {
        return try {
            val items = itemService.getAllItemsByUserId(userId)
            if (items.isEmpty()) {
                val errorResponse = mapOf("error" to "No items found for user ID: $userId")
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
            } else {
                ResponseEntity.ok(items)
            }
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @ExceptionHandler(value = [IllegalArgumentException::class, NoSuchElementException::class])
    fun handleException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }
}