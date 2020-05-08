package com.example.core

import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class FakeRepositoryMessangerIml :RepositoryMessanger {
companion object{
    val testUser= User(name = "Nurs")

}
    lateinit var channel : Channel<Message>

    override lateinit var domain: MessangerDomain

    override suspend fun sendMessage(chat: Chat, message: Message) {
        channel.send(message)
    }

    override suspend fun getChats(): Result<List<Chat>> {
        return Result.Success(listOf())
    }

    override suspend fun getChatMessages(chatId: String): Flow<Message> {
        return channel.consumeAsFlow()
    }

    override suspend fun getCurrentUser(onComplete: () -> Unit): Result<User> {
        return Result.Success(testUser)
    }
}