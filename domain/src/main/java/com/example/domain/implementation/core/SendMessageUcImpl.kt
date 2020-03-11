package com.example.domain.implementation.core

import com.example.comunicator.Message
import com.example.data.logic.core.RepositoryMessanger
import com.example.domain.logic.core.SendMessageUseCase

class SendMessageUcImpl(val repositoryMessanger: RepositoryMessanger) : SendMessageUseCase {
    override fun invoke(message: Message) {
        repositoryMessanger.sendMessage(message)
    }
}