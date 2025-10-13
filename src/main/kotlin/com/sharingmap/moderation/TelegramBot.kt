package com.sharingmap.telegram_notification

import com.sharingmap.item.ItemService
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
import java.util.UUID

@Service
class TelegramService(
    @Value("\${telegram.bot.token}") private val botToken: String,
    @Value("\${telegram.bot.username}") private val botUsername: String,
    private val itemService: ItemService
) : TelegramLongPollingBot(), ITelegramService {
    val logger = LoggerFactory.getLogger(TelegramService::class.java)

    override fun getBotUsername(): String = botUsername

    override fun getBotToken(): String = botToken

    override fun onUpdateReceived(update: Update?) {
        if (update?.hasCallbackQuery() == true) {
            val callbackData = update.callbackQuery.data
            if (callbackData.startsWith("DELETE", 0)) {
                val splitted = callbackData.split(":")
                if (splitted.size == 2 ) {
                    deleteItem(UUID.fromString(splitted[1]))
                    sendMessageOnDelete("-1002250304627", "Объявление было удалено " + splitted[1])
                }
            }
        }
    }

    @Async
    override fun sendModerationMessage(chatId: String, message: String, itemId: UUID) {
        val message = SendMessage(chatId, message)

        val markupInline = InlineKeyboardMarkup()
        val rowsInline = ArrayList<List<InlineKeyboardButton>>()
        val rowInline = ArrayList<InlineKeyboardButton>()

        val deleteButton = InlineKeyboardButton()
        deleteButton.text = "Удалить объявление"
        deleteButton.callbackData = "DELETE:$itemId"

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

    fun deleteItem(itemId: UUID) {
        println("Item was deleted")
        itemService.adminDeleteItem(itemId)
        // Your promotion logic here
    }

    override fun sendMessageOnDelete(chatId: String, message: String) {
        val message = SendMessage(chatId, message)

        try {
            execute(message)
        } catch (e: TelegramApiException) {
            logger.error("Failed to send telegram message", e)
        }
    }
}