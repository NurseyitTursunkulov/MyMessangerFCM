package com.example.core.domain.logic.core

import androidx.lifecycle.LiveData
import com.example.comunicator.Message

interface MessangerDomain {
    val newMessages: LiveData<Message>
    suspend fun sendMessage(message: Message)
    fun observeNewMessages()
    fun unsubscribeFromNewMessages()
    fun onNewMessageRecieved(message: Message)
}