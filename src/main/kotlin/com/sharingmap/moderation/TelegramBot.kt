package com.sharingmap.telegram_notification

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.springframework.scheduling.annotation.Async
import org.slf4j.LoggerFactory

@Service
class TelegramService(
    @Value("\${telegram.bot.token}") private val botToken: String,
    @Value("\${telegram.bot.username}") private val botUsername: String,
) : TelegramLongPollingBot(), ITelegramService {
    val logger = LoggerFactory.getLogger(TelegramService::class.java)

    override fun getBotUsername(): String = botUsername

    override fun getBotToken(): String = botToken

    override fun onUpdateReceived(update: Update?) {
        if (update?.hasCallbackQuery() == true) {
            val callbackData = update.callbackQuery.data
            if (callbackData == "DELETE") {
                DeleteItem()
            }
        }
    }

    @Async
    override fun sendModerationMessage(chatId: String, message: String) {
        val message = SendMessage(chatId, message)

        val markupInline = InlineKeyboardMarkup()
        val rowsInline = ArrayList<List<InlineKeyboardButton>>()
        val rowInline = ArrayList<InlineKeyboardButton>()

        val deleteButton = InlineKeyboardButton()
        deleteButton.text = "Удалить объявление"
        deleteButton.callbackData = "DELETE"

        rowInline.add(deleteButton)
        rowsInline.add(rowInline)

        markupInline.keyboard = rowsInline
        message.replyMarkup = markupInline
        try {
            execute(message)
        } catch (e: TelegramApiException) {
            logger.error("Failed to send telegram message", e)
        }
    }

    fun DeleteItem() {
        println("Item was deleted")
        // Your promotion logic here
    }
}