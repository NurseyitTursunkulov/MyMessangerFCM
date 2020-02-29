package com.example.domain.logic.core

import com.example.domain.logic.User

interface RecieveMessageUseCase {
    operator fun invoke(message: String, user: User)
}