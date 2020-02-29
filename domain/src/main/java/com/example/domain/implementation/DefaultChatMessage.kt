package com.example.domain.implementation

import com.example.domain.logic.ChatMessage
import com.example.domain.logic.User
import com.example.domain.logic.status.Status

class DefaultChatMessage(
    override var sender: User,
    override var message: String,
    override var status: Status
) : ChatMessage {
}