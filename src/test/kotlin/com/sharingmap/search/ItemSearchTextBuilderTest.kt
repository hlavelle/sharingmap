package com.sharingmap.search

import com.sharingmap.adresses.AddressEntity
import com.sharingmap.category.CategoryEntity
import com.sharingmap.city.CityEntity
import com.sharingmap.item.ItemEntity
import com.sharingmap.item.State
import com.sharingmap.subcategory.SubcategoryEntity
import com.sharingmap.user.Role
import com.sharingmap.user.UserEntity
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ItemSearchTextBuilderTest {
    private val builder = ItemSearchTextBuilder()

    @Test
    fun `build includes item and filter text and normalizes whitespace`() {
        val city = CityEntity().apply { name = "Москва" }
        val user = UserEntity(
            username = "user",
            email = "user@example.com",
            role = Role.ROLE_USER,
            password = "password",
            enabled = true
        )
        val address = AddressEntity(
            name = "home",
            description = "  рядом   с метро  ",
            user = user,
            city = city
        )
        val item = ItemEntity(
            name = "Детский велосипед",
            text = "Подойдет для прогулок",
            categories = setOf(CategoryEntity(name = "sport", description = "Спорт")),
            subcategory = SubcategoryEntity(name = "give", description = "Отдам"),
            city = city,
            locations = emptySet(),
            user = user,
            address = address,
            state = State.ACTIVE
        )

        val text = builder.build(item)

        assertTrue(text.contains("Детский велосипед"))
        assertTrue(text.contains("Подойдет для прогулок"))
        assertTrue(text.contains("sport"))
        assertTrue(text.contains("Спорт"))
        assertTrue(text.contains("Отдам"))
        assertTrue(text.contains("Москва"))
        assertTrue(text.contains("home"))
        assertTrue(text.contains("рядом с метро"))
        assertFalse(text.contains("  "))
    }

    @Test
    fun `build handles nullable optional fields`() {
        val city = CityEntity().apply { name = "Самара" }
        val item = ItemEntity(
            name = "Книга",
            text = null,
            categories = emptySet(),
            subcategory = null,
            city = city,
            locations = emptySet(),
            user = null,
            state = State.ACTIVE
        )

        val text = builder.build(item)

        assertTrue(text.contains("Книга"))
        assertTrue(text.contains("Самара"))
    }
}
