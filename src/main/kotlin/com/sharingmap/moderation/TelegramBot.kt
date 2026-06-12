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
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Service
class TelegramService(
    @Value("\${telegram.bot.token}") private val botToken: String,
    @Value("\${telegram.bot.username}") private val botUsername: String,
    private val itemService: ItemService
) : TelegramLongPollingBot(), ITelegramService {

    private val logger = LoggerFactory.getLogger(TelegramService::class.java)

    // dedicated pool for fire-and-forget telegram sends
    private val executor: Executor = Executors.newFixedThreadPool(4)

    override fun getBotUsername(): String = botUsername
    override fun getBotToken(): String = botToken

    override fun sendModerationMessage(chatId: String, messageText: String, itemId: UUID) {
        // returns immediately; work happens on the executor
        executor.execute {
            val sendMessage = SendMessage(chatId, messageText)

            val markupInline = InlineKeyboardMarkup()
            val deleteButton = InlineKeyboardButton().apply {
                text = "Удалить объявление"
                callbackData = "DELETE:$itemId"
            }
            markupInline.keyboard = listOf(listOf(deleteButton))
            sendMessage.replyMarkup = markupInline

            try {
                execute(sendMessage)
            } catch (e: TelegramApiException) {
                logger.error("Failed to send telegram moderation message for item {}", itemId, e)
            } catch (e: Exception) {
                // catch-all so the executor thread never dies silently
                logger.error("Unexpected error sending moderation message for item {}", itemId, e)
            }
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

    override fun onUpdateReceived(update: Update?) {
        TODO("Not yet implemented")
    }
}