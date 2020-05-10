package com.example.mymessangerfcm

import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class FakeDomain : MessangerDomain {
    override var currentChat: Chat? = null
    lateinit var channel : Channel<Message>
    val currentUser = User()
    override suspend fun sendMessage(chat: Chat, message: Message) {
        channel.send(message)
    }

    override suspend fun getChatsChannels(): Result<List<Chat>> {
        return Result.Success(listOf())
    }

    override suspend fun getChatMessagesFlow(chatId: String): Flow<Message> {
        return channel.consumeAsFlow()
    }

    override suspend fun getCurrentUser(onComplete: () -> Unit): Result<User> {
        return Result.Success(currentUser)
    }
}