package com.example.core.domain.logic.core

import androidx.lifecycle.LiveData
import com.example.core.comunicator.Message
import kotlinx.coroutines.flow.Flow
import com.example.core.comunicator.Result
import kotlinx.coroutines.CoroutineScope

interface MessangerDomain  {
    var currentChat:Chat?
    suspend fun sendMessage(chat: Chat,message: Message)
    suspend fun getChatsChannels():Result<List<Chat>>
    suspend fun getChatMessagesFlow(
        chatId: String
    ): Flow<Message>
    suspend fun getCurrentUser(onComplete: () -> Unit): Result<User>
}