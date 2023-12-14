package com.sharingmap.item

import com.sharingmap.user.UserEntity
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import kotlin.NoSuchElementException
import java.util.UUID

@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping("/items/{id}")
    fun getItemById(@PathVariable id: UUID): ResponseEntity<Any> {
        return try {
            val item = toItemDto(itemService.getItemById(id))
            ResponseEntity.ok(item)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        }
    }


    @GetMapping("/items/all")
    fun getAllItems(@RequestParam(value = "categoryId", defaultValue = "0") categoryId: Long,
                    @RequestParam(value = "cityId", defaultValue = "1") cityId: Long,
                    @RequestParam(value = "subcategoryId", defaultValue = "1") subcategoryId: Long,
                    @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                    @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<Page<ItemDto>> {
        val items =  itemService.getAllItems(categoryId, subcategoryId, cityId, page, size)
        val itemDtos = items.map { toItemDto(it) }
        return ResponseEntity.ok(itemDtos)
    }

    @PostMapping("/items/create")
    fun createItem(@RequestBody item: ItemEntity): ResponseEntity<String>  {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        if (user.id == null) {
            ResponseEntity.notFound()
        }
        item.user = user
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
    fun getAllItemsByUserId(@PathVariable(value = "userId") @Min(1) userId: UUID,
                            @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                            @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int):
            ResponseEntity<Any> {
        return try {
            val items = itemService.getAllItemsByUserId(userId, page, size)
            if (items.isEmpty()) {
                val errorResponse = mapOf("error" to "No items found for user ID: $userId")
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
            } else {
                val itemDtos = items.map { toItemDto(it) }
                ResponseEntity.ok(itemDtos)
            }
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @ExceptionHandler(value = [IllegalArgumentException::class, NoSuchElementException::class])
    fun handleException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    private fun toItemDto(itemEntity: ItemEntity): ItemDto {
        return ItemDto(
            id = itemEntity.id,
            name = itemEntity.name,
            text = itemEntity.text,
            locationId = itemEntity.location?.id,
            imagesId = itemEntity.images?.mapNotNull { it.id } ?: listOf(),
            createdAt = itemEntity.createdAt,
            updatedAt = itemEntity.updatedAt,
            categoryId = itemEntity.category?.id,
            subcategoryId = itemEntity.subcategory?.id,
            cityId = itemEntity.city?.id,
            userId = itemEntity.user?.id
        )
    }
}