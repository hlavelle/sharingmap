package com.sharingmap.search

import com.sharingmap.item.ItemEntity
import org.springframework.stereotype.Component

@Component
class ItemSearchTextBuilder {
    fun build(item: ItemEntity): String {
        val parts = mutableListOf<String?>()
        parts += item.name
        parts += item.text
        parts += item.categories.flatMap { listOf(it.name, it.description) }
        parts += item.subcategory?.name
        parts += item.subcategory?.description
        parts += item.city.name
        parts += item.address?.name
        parts += item.address?.description
        return parts
            .asSequence()
            .filterNotNull()
            .map { normalizeWhitespace(it) }
            .filter { it.isNotBlank() }
            .distinct()
            .joinToString(" ")
    }

    private fun normalizeWhitespace(value: String): String {
        return value.trim().replace(Regex("\\s+"), " ")
    }
}
