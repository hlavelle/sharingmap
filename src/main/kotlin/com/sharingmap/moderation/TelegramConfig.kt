package com.sharingmap.telegram_notification

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync(proxyTargetClass = true)
class TelegramConfig {
    @Bean
    fun telegramBotsApi(telegramService: TelegramService): TelegramBotsApi {
        val api = TelegramBotsApi(DefaultBotSession::class.java)
        try {
            api.registerBot(telegramService)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
        return api
    }
}
