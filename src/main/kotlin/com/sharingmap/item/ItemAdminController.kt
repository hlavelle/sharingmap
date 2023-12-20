package com.sharingmap.item

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.TransactionException
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ItemAdminController(private val itemService: ItemService) {

    @PostMapping("/admin/items/create/{userId}")
    fun adminCreateItem(@PathVariable userId: UUID, @RequestBody item: ItemCreateDto): ResponseEntity<Any> {
        return try {
            val createdItem = itemService.createItem(userId, item)
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

    @DeleteMapping("/admin/items/delete")
    fun adminDeleteItem(@PathVariable itemId: UUID): ResponseEntity<Any> {
        return try {
            itemService.adminDeleteItem(itemId)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Item not found with ID: $itemId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/admin/items/update")
    fun adminUpdateItem(@RequestBody item: ItemUpdateDto): ResponseEntity<Any> {
        return try {
            itemService.adminUpdateItem(item)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

}