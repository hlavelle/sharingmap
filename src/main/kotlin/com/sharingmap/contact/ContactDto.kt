package com.sharingmap.contact

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.*

data class ContactDto(
    @field:NotBlank(message = "Contact cannot be blank")
    val contact: String,
    @field:NotNull(message = "Type cannot be null")
    @field:Pattern(
        regexp = "^(WHATSAPP|TELEGRAM|PHONE)$",
        message = "Type must be one of: WHATSAPP, TELEGRAM, PHONE"
    )
    val type: TypeContact)

data class ContactGetDto(val contact: String, val type: TypeContact, val id: UUID?)

data class ContactUpdateDto(
    val contact: String?,
    @field:Pattern(
        regexp = "^(WHATSAPP|TELEGRAM|PHONE)$",
        message = "Type must be one of: WHATSAPP, TELEGRAM, PHONE"
    )
    val type: TypeContact?)