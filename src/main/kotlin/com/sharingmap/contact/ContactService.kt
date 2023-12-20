package com.sharingmap.contact

import com.sharingmap.contact.ContactEntity
import java.util.*

interface ContactService {
    fun getContactById(id: UUID): ContactEntity
    fun getAllUserContacts(id: UUID): List<ContactEntity>
    fun createContact(userId: UUID, contact: ContactDto): ContactDto
    fun adminDeleteContact(contactId: UUID)
    fun deleteContact(userId: UUID, contactId: UUID)
    fun adminUpdateContact(contact: ContactUpdateDto) : ContactEntity
    fun updateContact(userId: UUID, contact: ContactUpdateDto) : ContactDto
}