package com.sharingmap.contact

import java.util.*

data class ContactDto(val contact: String, val type: TypeContact)

data class ContactGetDto(val contact: String, val type: TypeContact, val id: UUID?)

data class ContactUpdateDto(val id: UUID, val contact: String?, val type: TypeContact?)