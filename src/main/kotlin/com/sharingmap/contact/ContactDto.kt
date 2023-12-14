package com.sharingmap.contact

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.*

data class ContactDto(val contact: String, val type: TypeContact)

data class ContactGetDto(val contact: String, val type: TypeContact, val id: UUID?)

data class ContactUpdateDto(val contact: String?, val type: TypeContact?)