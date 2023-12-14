package com.sharingmap.item

import com.sharingmap.user.UserNotFoundException
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.TransactionException
import org.springframework.web.bind.annotation.*
import java.util.*

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
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }


    @GetMapping("/items/all")
    fun getAllItems(@RequestParam(value = "categoryId", defaultValue = "0") categoryId: Long,
                    @RequestParam(value = "cityId", defaultValue = "1") cityId: Long,
                    @RequestParam(value = "subcategoryId", defaultValue = "1") subcategoryId: Long,
                    @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                    @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<Any> {
        return try {
            val items = itemService.getAllItems(categoryId, subcategoryId, cityId, page, size)
            val itemDtos = items.map { toItemDto(it) }
            ResponseEntity.ok(itemDtos)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @PostMapping("/items/create")
    @PreAuthorize("#id == principal.id")
    fun createItem(@RequestParam id: UUID, @Valid @RequestBody item: ItemCreateDto): ResponseEntity<Any>  {
        return try {
            val createdItem = itemService.createItem(id, item)
            val itemId = createdItem?.id
            ResponseEntity.status(HttpStatus.CREATED).body(itemId.toString())
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: DataAccessException) {
            throw RuntimeException("Error occurred while creating the item.", ex)
        } catch (ex: TransactionException) {
            throw RuntimeException("Transaction failed while creating the item.", ex)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @DeleteMapping("/items/{itemId}/delete")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun deleteItem(@RequestParam id: UUID, @PathVariable itemId: UUID): ResponseEntity<Any> {
        return try {
            itemService.deleteItem(itemId)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $itemId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/items/{itemId}/update")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun updateItem(@RequestParam id: UUID, @PathVariable itemId: UUID, @RequestBody item: ItemUpdateDto): ResponseEntity<Any> {
        return try {
            itemService.updateItem(itemId, item)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @GetMapping("/users/{userId}/items")
    fun getAllItemsByUserId(@PathVariable(value = "userId") userId: UUID,
                            @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                            @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int):
            ResponseEntity<Any> {
        return try {
            val items = itemService.getAllItemsByUserId(userId, page, size)
            if (items.isEmpty) {
                val errorResponse = mapOf("error" to "No items found for user ID: $userId")
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
            } else {
                val itemDtos = items.map { toItemDto(it) }
                ResponseEntity.ok(itemDtos)
            }
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
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