package com.sharingmap.item

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.NoSuchElementException
import java.util.UUID

@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/items/{id}")
    fun getItemById(@PathVariable id: UUID): ResponseEntity<Any> {
        return try {
            val item = itemService.getItemById(id)
            ResponseEntity.ok(item)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        }
    }


    @GetMapping("/items/all")
    fun getAllItems(@RequestParam(value = "categoryId", defaultValue = "1") categoryId: Long,
                    @RequestParam(value = "cityId", defaultValue = "1") cityId: Long,
                    @RequestParam(value = "subcategoryId", defaultValue = "1") subcategoryId: Long,
                    @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                    @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<List<ItemEntity>> {
        val items =  itemService.getAllItems(categoryId, subcategoryId, cityId, page, size)
        return ResponseEntity.ok(items)
    }

    @PostMapping("/items/create")
    fun createItem(@RequestBody @Valid item: ItemEntity): ResponseEntity<String>  {
        val createdItem = itemService.createItem(item)
        val itemId = createdItem?.id
        return ResponseEntity.status(HttpStatus.CREATED).body(itemId.toString())
    }

    @DeleteMapping("/items/{id}/delete")
    fun deleteItem(@PathVariable @Min(1) id: UUID): ResponseEntity<Unit> {
        val isDeleted = itemService.deleteItem(id)

        return if (isDeleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/items/{id}/update")
    fun updateItem(@RequestBody item: ItemEntity): ResponseEntity<Unit> {
        itemService.updateItem(item)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/users/{userId}/items")
    fun getAllItemsByUserId(@PathVariable(value = "userId") @Min(1) userId: UUID):  ResponseEntity<Any> {
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