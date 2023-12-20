package com.sharingmap.contact

import com.sharingmap.contact.ContactEntity
import com.sharingmap.contact.ContactRepository
import com.sharingmap.contact.ContactService
import com.sharingmap.user.UserNotFoundException
import com.sharingmap.user.UserRepository
import com.sharingmap.user.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContactServiceImpl(private val contactRepository: ContactRepository,
                         private val userService: UserService,
                         private val userRepository: UserRepository): ContactService {
    override fun getContactById(id: UUID): ContactEntity {
        return contactRepository.findById(id)
            .orElseThrow { NoSuchElementException("Contact not found with ID: $id") }
    }

    override fun getAllUserContacts(id: UUID): List<ContactEntity> {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with ID: $id") }

        return contactRepository.findAllByUser(user)
    }

    override fun createContact(userId: UUID, contact: ContactDto) : ContactDto {
        val user = userService.getUserById(userId)
        val newContact = ContactEntity(contact = contact.contact,
            type = contact.type, user = user)
        contactRepository.save(newContact)
        return ContactDto(contact = newContact.contact, type = newContact.type, id = newContact.id)
    }

    override fun adminDeleteContact(contactId: UUID) {
        contactRepository.findById(contactId).orElseThrow { NoSuchElementException("Contact not found with ID: $contactId") }
        contactRepository.deleteById(contactId)
    }

    override fun deleteContact(userId: UUID, contactId: UUID) {
        val contact = contactRepository.findById(contactId)
            .orElseThrow{ NoSuchElementException("Contact not found with ID: $contactId") }
        if (userId != contact.user.id) {
            throw IllegalArgumentException("This user does not have permission to delete this contact.")
        }
        contactRepository.deleteById(contactId)
    }

    override fun adminUpdateContact(contact: ContactUpdateDto) : ContactEntity {
        val newContact = contactRepository.findById(contact.id)
            .orElseThrow { NoSuchElementException("Contact not found with ID: ${contact.id}") }

        if (contact.contact != null) newContact.contact = contact.contact
        if (contact.type != null) newContact.type = contact.type

        return contactRepository.save(newContact)
    }

    override fun updateContact(userId: UUID, contact: ContactUpdateDto) : ContactDto {
        val newContact = contactRepository.findById(contact.id)
            .orElseThrow { NoSuchElementException("Contact not found with ID: ${contact.id}") }
        if (userId != newContact.user.id) {
            throw IllegalArgumentException("This user does not have permission to update this contact.")
        }

        if (contact.contact != null) newContact.contact = contact.contact
        if (contact.type != null) newContact.type = contact.type

        contactRepository.save(newContact)
        return ContactDto(contact = newContact.contact, type = newContact.type, id = newContact.id)
    }
}