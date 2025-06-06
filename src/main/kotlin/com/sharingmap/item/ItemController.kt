package com.sharingmap.item

import com.sharingmap.image.ImageService
import com.sharingmap.image.ItemImageEntity
import com.sharingmap.image.UserImageEntity
import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserNotFoundException
import com.sharingmap.telegram_notification.*
import com.sharingmap.user.UserService
import jakarta.validation.constraints.Min
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.TransactionException
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ItemController(
    private val itemService: ItemService,
    private val moderationService: ITelegramService,
    private val itemImageService: ImageService<ItemImageEntity>,
    private val userService: UserService
        ) {

    @GetMapping("/items/{itemId}")
    fun getItemById(@PathVariable itemId: UUID): ResponseEntity<Any> {
        return try {
            val item = toItemDto(itemService.getItemById(itemId))
            ResponseEntity.ok(item)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $itemId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }


    @GetMapping("/items/all")
    fun getAllActiveItems(@RequestParam(value = "categoryId", defaultValue = "0") categoryId: Long,
                    @RequestParam(value = "cityId", defaultValue = "1") cityId: Long,
                    @RequestParam(value = "subcategoryId", defaultValue = "1") subcategoryId: Long,
                    @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                    @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int
    ): ResponseEntity<Any> {
        return try {
            val items = itemService.getAllActiveItemsByEnabledUsers(categoryId, subcategoryId, cityId, page, size)
            val itemDtos = items.map { toItemDto(it) }
            ResponseEntity.ok(itemDtos)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @PostMapping("/items/create")
    fun createItem(@RequestBody item: ItemCreateDto): ResponseEntity<Any>  {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity

            val createdItem = user.id.let { itemService.createItem(it, item) }
            val itemId = createdItem?.id
            val itemText = "название: ${createdItem?.name} \n описание: ${createdItem?.text} \n пользователь: ${user.username} \n город ${createdItem?.city?.name}"
            moderationService.sendModerationMessage("-1002250304627", itemText)
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

    @DeleteMapping("/items/delete/{itemId}")
    fun deleteItem(@PathVariable itemId: UUID,
                   @RequestParam(value = "isGiftedOnSm", defaultValue = "false") isGiftedOnSm: Boolean
    ): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            user.id.let { itemService.deleteItem(it, itemId, isGiftedOnSm) }
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: IllegalArgumentException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $itemId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/items/update")
    fun updateItem(@RequestBody item: ItemUpdateDto): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            user.id?.let { itemService.updateItem(it, item) }
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: IllegalArgumentException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @GetMapping("/users/{userId}/items")
    fun getAllActiveItemsByUserId(@PathVariable(value = "userId") userId: UUID,
                            @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
                            @RequestParam(value = "size", defaultValue = "10") @Min(1) size: Int):
            ResponseEntity<Any> {
        return try {
            val items = itemService.getAllActiveItemsByUserId(userId, page, size)
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
            locationsId = itemEntity.locations.mapNotNull { it.id },
            imagesId = itemEntity.images?.map { it.id } ?: listOf(),
            createdAt = itemEntity.createdAt,
            updatedAt = itemEntity.updatedAt,
            categoriesId = itemEntity.categories.mapNotNull { it.id },
            subcategoryId = itemEntity.subcategory?.id,
            cityId = itemEntity.city.id,
            userId = itemEntity.user?.id,
            user = itemEntity.user?.let{user->userService.toItemUserDto(user)},
            itemPhoto = itemEntity.images?.map { itemImageService.toImageDto(it) }
        )
    }
}