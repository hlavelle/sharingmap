package com.sharingmap.contact


import com.sharingmap.user.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ContactController(private val contactService: ContactService) {
    @GetMapping("/contacts/{id}")
    fun getContactById(@PathVariable id: UUID): ResponseEntity<Any> {
        return try {
            val contact = contactService.getContactById(id)
            val contactDto =  toContactGetDto(contact)
            ResponseEntity.ok(contactDto)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Contact not found with ID: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @GetMapping("/users/{userId}/contacts")
    fun getAllUserContacts(@PathVariable userId: UUID): ResponseEntity<Any> {
        return try {
            val contacts = contactService.getAllUserContacts(userId)
            val contactDtos = contacts.map { toContactGetDto(it) }
            ResponseEntity.ok(contactDtos)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @PostMapping("/contacts/create")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id") //TODO проверка на одинаковый контакт
    fun createContact(@RequestParam id: UUID, @RequestBody contact: ContactDto): ResponseEntity<Any> {
        return try {
            contactService.createContact(id, contact)
            ResponseEntity.status(HttpStatus.CREATED).body("Contact created successfully")
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @DeleteMapping("/contacts/delete/{contactId}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun deleteContact(@RequestParam id: UUID, @PathVariable contactId: UUID): ResponseEntity<Any> {
        return try {
            contactService.deleteContact(contactId)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Contact not found with ID: $contactId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/contacts/update/{contactId}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    fun updateContact(@RequestParam id: UUID, @PathVariable contactId: UUID, @RequestBody contact: ContactUpdateDto)
    : ResponseEntity<Any> {
        return try {
            contactService.updateContact(contactId, contact)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Contact not found with ID: $contactId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    private fun toContactGetDto(contactEntity: ContactEntity): ContactGetDto{
        return ContactGetDto(
            contact = contactEntity.contact,
            type = contactEntity.type,
            id = contactEntity.id
        )
    }
}