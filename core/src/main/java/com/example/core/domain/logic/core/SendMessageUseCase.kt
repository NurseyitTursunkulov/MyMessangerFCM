package com.example.domain.logic.core

import com.example.comunicator.Message

interface SendMessageUseCase {
    operator fun invoke(message: Message)
}