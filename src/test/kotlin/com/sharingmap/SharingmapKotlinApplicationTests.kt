package com.sharingmap

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

// Boots the full Spring context, which needs Postgres (PG_CONNECTION_STRING), SMTP
// (MAIL_PASSWORD), APP_SECRET, a Telegram bot token and Firebase credentials — none of
// which exist in CI. Either give it a test profile with Testcontainers and mocked
// mail/Telegram/Firebase, or delete it. See migration task P6-T9.
@SpringBootTest
@Disabled("Needs a live environment (Postgres, SMTP, APP_SECRET, Telegram, Firebase). See P6-T9.")
class SharingmapKotlinApplicationTests {

    @Test
    fun contextLoads() {
    }

}
