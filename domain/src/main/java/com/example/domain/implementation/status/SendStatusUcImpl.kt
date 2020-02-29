package com.example.domain.implementation.status

import com.example.domain.logic.User
import com.example.domain.logic.core.RepositoryMessanger
import com.example.domain.logic.core.SendMessageUseCase

class SendStatusUcImpl(val repository: RepositoryMessanger):SendMessageUseCase {
    override fun invoke(message: String, user: User) {
        repository.sendStatus(message,user)
    }
}