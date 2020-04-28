package com.example.core.domain.logic.core

import androidx.lifecycle.LiveData
import com.example.core.comunicator.Message
import kotlinx.coroutines.flow.Flow

interface MessangerDomain : LifeCycleObserver {
    val newMessages: LiveData<Message>
    suspend fun sendMessage(message: Message)
    fun onNewMessageRecieved(message: Message)

    suspend fun getChatsChannel():List<Chat>
    suspend fun getChatMessages(chatId:String): Flow<Message>
}