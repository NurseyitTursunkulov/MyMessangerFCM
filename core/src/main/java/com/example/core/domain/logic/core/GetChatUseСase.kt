package com.example.domain.logic.core

import com.example.domain.logic.ChatMessage
import com.example.domain.logic.User

interface GetChatUseСase {
    operator fun invoke(sender: User, reciever: User):List<ChatMessage>
}