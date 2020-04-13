package com.example.core.data.logic.core

import com.example.comunicator.Message
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain

interface RepositoryMessanger {
    var domain: MessangerDomain
    fun sendMessage(message: Message)
    fun subscribeForNewMessages()
    fun unsubscribe()
    suspend fun getChats():List<Chat>
}
