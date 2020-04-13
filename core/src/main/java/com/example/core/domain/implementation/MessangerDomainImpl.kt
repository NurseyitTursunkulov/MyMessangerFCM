package com.example.core.domain.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.comunicator.Message
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain

class MessangerDomainImpl(
    val repositoryMessanger: RepositoryMessanger
) : MessangerDomain {

    init {
        repositoryMessanger.domain = this
    }

    private val _newMessages: MutableLiveData<Message> = MutableLiveData()
    override val newMessages: LiveData<Message> = _newMessages

    override suspend fun sendMessage(message: Message) {
        repositoryMessanger.sendMessage(message)
    }

    override fun observeNewMessages() {
        repositoryMessanger.subscribeForNewMessages()
    }

    override fun unsubscribeFromNewMessages() {
        repositoryMessanger.unsubscribe()
    }

    override fun onNewMessageRecieved(message: Message) {
        _newMessages.postValue(message)
    }

    override suspend fun getChats(): List<Chat> =
        repositoryMessanger.getChats()

}