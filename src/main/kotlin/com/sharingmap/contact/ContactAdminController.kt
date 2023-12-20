package com.sharingmap.contact

import com.sharingmap.user.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
@RestController
class ContactAdminController(private val contactService: ContactService) {

    @PostMapping("/admin/contacts/create/{userId}")
    fun adminCreateContact(@PathVariable userId: UUID, @RequestBody contact: ContactDto): ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(contactService.createContact(userId, contact))
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @DeleteMapping("/admin/contacts/delete/{contactId}")
    fun adminDeleteContact(@PathVariable contactId: UUID): ResponseEntity<Any> {
        return try {
            contactService.adminDeleteContact(contactId)
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Contact not found with ID: $contactId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/admin/contacts/update")
    fun adminUpdateContact(@RequestBody contact: ContactUpdateDto)
            : ResponseEntity<Any> {
        return try {
            ResponseEntity.status(HttpStatus.OK).body(contactService.adminUpdateContact(contact))
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }
}