package com.example.domain.core

import com.example.domain.User

interface SendMessageUseCase {
    operator fun invoke(message: String, user: User)
}