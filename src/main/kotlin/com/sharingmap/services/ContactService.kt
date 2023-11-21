package com.sharingmap.services

import com.sharingmap.entities.CityEntity
import com.sharingmap.entities.ContactEntity
import java.util.*

interface ContactService {
    fun getContactById(id: UUID): ContactEntity
    fun getAllUserContacts(id: UUID): List<ContactEntity>
    fun createContact(contact: ContactEntity)
    fun deleteContact(id: UUID)
    fun updateContact(id: UUID, contact: ContactEntity)
}