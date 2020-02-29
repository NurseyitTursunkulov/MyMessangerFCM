package com.example.domain.core

import com.example.domain.ChatMessage
import com.example.domain.User

interface GetChatUseСase {
    operator fun invoke(sender:User,reciever:User):List<ChatMessage>
}