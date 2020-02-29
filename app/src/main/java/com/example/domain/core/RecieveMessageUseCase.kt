package com.example.domain.core

import com.example.domain.User

interface RecieveMessageUseCase {
    operator fun invoke(message: String, user: User)
}