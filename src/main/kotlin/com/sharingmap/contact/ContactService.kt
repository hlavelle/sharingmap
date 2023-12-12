package com.sharingmap.contact

import com.sharingmap.contact.ContactEntity
import java.util.*

interface ContactService {
    fun getContactById(id: UUID): ContactEntity
    fun getAllUserContacts(id: UUID): List<ContactEntity>
    fun createContact(id: UUID, contact: ContactDto)
    fun deleteContact(id: UUID)
    fun updateContact(id: UUID, contact: ContactUpdateDto)
}