package com.sharingmap.telegram_notification

import java.util.UUID

interface ITelegramService {
    fun sendModerationMessage(chatId: String, message: String, itemId: UUID)
    fun sendMessageOnDelete(chatId: String, message: String)
}