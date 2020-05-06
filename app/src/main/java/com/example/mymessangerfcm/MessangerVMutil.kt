package com.example.mymessangerfcm

import androidx.lifecycle.viewModelScope
import com.example.core.comunicator.Message
import com.example.core.domain.logic.core.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun MessangerViewModel.launchCoroutineOnDispatcherIO(function: suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.IO) {
            function()
        }
    }
}

fun isNewMessage(
    newMsg: Message,
    chat: Chat
): Boolean = newMsg !in chat.messages

fun MessangerViewModel.isNotMyMessage(newMessage: Message): Boolean =
    newMessage.senderId != currentUser?.id