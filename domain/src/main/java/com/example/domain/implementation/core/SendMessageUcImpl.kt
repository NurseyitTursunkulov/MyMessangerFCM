package com.example.domain.implementation.core

import com.example.domain.logic.User
import com.example.domain.logic.core.RepositoryMessanger
import com.example.domain.logic.core.SendMessageUseCase

class SendMessageUcImpl(val repositoryMessanger: RepositoryMessanger) : SendMessageUseCase {
    override fun invoke(message: String, user: User) {
        repositoryMessanger.sendMessage(message,user)
    }
}