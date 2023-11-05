package com.sharingmap.controllers


import com.sharingmap.entities.ContactEntity
import com.sharingmap.services.ContactService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ContactController(private val contactService: ContactService) {
    @GetMapping("/contacts/{id}")
    fun getContactById(@PathVariable id: UUID): ContactEntity {
        return contactService.getContactById(id)
    }

    @GetMapping("/contacts/all")
    fun getAllUserContacts(@PathVariable userId: UUID): List<ContactEntity> {
        return contactService.getAllUserContacts(userId)
    }

    @PostMapping("/contacts/create")
    fun createContact(@RequestBody contact: ContactEntity) {
        contactService.createContact(contact)
    }

    @DeleteMapping("/contacts/delete/{id}")
    fun deleteContact(@PathVariable id: UUID) {
        contactService.deleteContact(id)
    }

    @PutMapping("/contacts/update/{id}")
    fun updateContact(@PathVariable id: UUID, @RequestBody contact: ContactEntity) {
        contactService.updateContact(id, contact)
    }
}