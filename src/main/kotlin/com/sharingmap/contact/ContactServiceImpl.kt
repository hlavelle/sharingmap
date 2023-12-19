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

    override fun createContact(id: UUID, contact: ContactDto) : ContactEntity {
        val user = userService.getUserById(id)
        val newContact = ContactEntity(contact = contact.contact,
            type = contact.type, user = user)
        return contactRepository.save(newContact)
    }

    override fun deleteContact(id: UUID) {
        contactRepository.findById(id).orElseThrow { NoSuchElementException("Contact not found with ID: $id") }
        contactRepository.deleteById(id)
    }

    override fun updateContact(id: UUID, contact: ContactUpdateDto) : ContactEntity {
        val newContact = contactRepository.findById(id)
            .orElseThrow { NoSuchElementException("Contact not found with ID: $id") }

        if (contact.contact != null) newContact.contact = contact.contact
        if (contact.type != null) newContact.type = contact.type

        return contactRepository.save(newContact)
    }
}