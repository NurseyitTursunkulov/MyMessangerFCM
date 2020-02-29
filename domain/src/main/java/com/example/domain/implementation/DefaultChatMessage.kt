package com.example.domain.implementation

import com.example.domain.logic.ChatMessage
import com.example.domain.logic.User

class DefaultChatMessage(override var user: User, override var message: String) : ChatMessage {
}