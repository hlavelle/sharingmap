package com.sharingmap.services

import com.sharingmap.entities.ContactEntity
import com.sharingmap.repositories.ContactRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContactServiceImpl(private val contactRepository: ContactRepository): ContactService {
    override fun getContactById(id: UUID): ContactEntity = contactRepository.findById(id).get()

    override fun getAllUserContacts(id: UUID): List<ContactEntity> {
        return contactRepository.findAllByUserId(id)
    }

    override fun createContact(contact: ContactEntity) {
        contactRepository.save(contact)
    }

    override fun deleteContact(id: UUID) {
        contactRepository.deleteById(id)
    }

    override fun updateContact(id: UUID, contact: ContactEntity) {
        var newContact = contactRepository.findById(id).get()
        newContact.contact = contact.contact
        newContact.type = contact.type
        contactRepository.save(newContact)
    }
}