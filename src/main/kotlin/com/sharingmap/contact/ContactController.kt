package com.sharingmap.contact


import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ContactController(private val contactService: ContactService) {
    @GetMapping("/contacts/{id}")
    fun getContactById(@PathVariable id: UUID): ContactEntity {
        return contactService.getContactById(id)
    }

    @GetMapping("/contacts/all") //TODO сделать проверку юзера и админ
    fun getAllUserContacts(@PathVariable userId: UUID): List<ContactEntity> {
        return contactService.getAllUserContacts(userId)
    }

    @PostMapping("/contacts/create") //TODO сделать проверку юзера и админ
    fun createContact(@RequestBody contact: ContactEntity) {
        contactService.createContact(contact)
    }

    @DeleteMapping("/contacts/delete/{id}") //TODO сделать проверку юзера и админ
    fun deleteContact(@PathVariable id: UUID) {
        contactService.deleteContact(id)
    }

    @PutMapping("/contacts/update/{id}") //TODO сделать проверку юзера и админ
    fun updateContact(@PathVariable id: UUID, @RequestBody contact: ContactEntity) {
        contactService.updateContact(id, contact)
    }
}