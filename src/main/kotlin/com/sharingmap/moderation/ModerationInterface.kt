package com.sharingmap.telegram_notification

interface ITelegramService {
    fun sendModerationMessage(chatId: String, message: String)
}