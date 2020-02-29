package com.example.domain.logic.core

import com.example.domain.logic.User

interface SendMessageUseCase {
    operator fun invoke(message: String, user: User)
}