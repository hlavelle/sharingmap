package com.sharingmap.contact


import com.sharingmap.user.UserEntity
import com.sharingmap.user.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ContactController(private val contactService: ContactService) {
    @GetMapping("/contacts/{contactId}")
    fun getContactById(@PathVariable contactId: UUID): ResponseEntity<Any> {
        return try {
            val contact = contactService.getContactById(contactId)
            val contactDto =  toContactDto(contact)
            ResponseEntity.ok(contactDto)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Contact not found with ID: $contactId")
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
            val contactDtos = contacts.map { toContactDto(it) }
            ResponseEntity.ok(contactDtos)
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            val errorResponse = mapOf("error" to "Internal Server Error")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }

    @GetMapping("/contacts/myself")
    fun getAllMyContacts(): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            val contacts = user.id?.let { contactService.getAllUserContacts(it) }
            val contactDtos = contacts?.map { toContactDto(it) }
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
    fun createContact(@RequestBody contact: ContactDto): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            ResponseEntity.status(HttpStatus.OK).body(user.id?.let { contactService.createContact(it, contact) })
        } catch (ex: UserNotFoundException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @DeleteMapping("/contacts/delete/{contactId}")
    fun deleteContact(@PathVariable contactId: UUID): ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            user.id?.let { contactService.deleteContact(it, contactId) }
            ResponseEntity.status(HttpStatus.OK).body(null)
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to "Contact not found with ID: $contactId")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: IllegalArgumentException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    @PutMapping("/contacts/update")
    fun updateContact(@RequestBody contact: ContactUpdateDto)
    : ResponseEntity<Any> {
        return try {
            if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
                ResponseEntity.badRequest()
            }
            val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
            if (user.id == null) {
                ResponseEntity.notFound()
            }
            ResponseEntity.status(HttpStatus.OK).body(user.id?.let { contactService.updateContact(it, contact) })
        } catch (ex: NoSuchElementException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (ex: IllegalArgumentException) {
            val errorResponse = mapOf("error" to ex.message)
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error")
        }
    }

    private fun toContactDto(contactEntity: ContactEntity): ContactDto{
        return ContactDto(
            contact = contactEntity.contact,
            type = contactEntity.type,
            id = contactEntity.id
        )
    }
}