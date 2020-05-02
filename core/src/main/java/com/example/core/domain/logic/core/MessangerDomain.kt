package com.example.core.domain.logic.core

import androidx.lifecycle.LiveData
import com.example.core.comunicator.Message
import kotlinx.coroutines.flow.Flow
import com.example.core.comunicator.Result
import kotlinx.coroutines.CoroutineScope

interface MessangerDomain : LifeCycleObserver {
    val newMessages: LiveData<Message>
    suspend fun sendMessage(chat: Chat,message: Message)
    fun onNewMessageRecieved(message: Message)

    suspend fun getChatsChannels():Result<List<Chat>>
    suspend fun getChatMessages(
        chatId: String
    ): Flow<Message>
    suspend fun getCurrentUser(onComplete: () -> Unit): Result<User>
}