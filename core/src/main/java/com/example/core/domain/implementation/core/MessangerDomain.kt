package com.example.domain.implementation.core

import com.example.comunicator.Message
import com.example.domain.logic.core.SendMessageUseCase

class MessangerDomain(
    val sendMessageUseCase: SendMessageUseCase
) {
     fun sendMessage(message: Message) {
        sendMessageUseCase.invoke(message)
    }
}