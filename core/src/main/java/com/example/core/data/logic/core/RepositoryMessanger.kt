package com.example.core.data.logic.core

import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import kotlinx.coroutines.flow.Flow

interface RepositoryMessanger {
    var domain: MessangerDomain
    fun sendMessage(message: Message)
    fun subscribeForNewMessages()
    fun unsubscribe()
    suspend fun getChats():List<Chat>
    suspend fun getChatMessages(chatId:String): Flow<Message>
    suspend fun getCurrentUser(onComplete: () -> Unit): Result<User>
}
