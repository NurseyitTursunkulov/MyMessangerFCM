package com.example.core.data.logic.core

import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface RepositoryMessanger {
    var domain: MessangerDomain
    suspend fun sendMessage(chat:Chat, message: Message)
    suspend fun getChats():Result<List<Chat>>
    suspend fun getChatMessages(
        chatId: String
    ): Flow<Message>
    suspend fun getCurrentUser(onComplete: () -> Unit): Result<User>
}
