package com.example.domain.logic.core

import androidx.lifecycle.LiveData
import com.example.domain.logic.ChatMessage
import com.example.domain.logic.User
import com.example.domain.logic.status.Status

interface RecieveMessageUseCase {
    operator fun invoke(message: String, user: User): LiveData<Status>
}