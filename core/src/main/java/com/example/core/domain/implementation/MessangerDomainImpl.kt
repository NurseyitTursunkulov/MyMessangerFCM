package com.example.core.domain.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class MessangerDomainImpl(
    val repositoryMessanger: RepositoryMessanger
) : MessangerDomain {

    init {
        repositoryMessanger.domain = this
    }

    private val _newMessages: MutableLiveData<Message> = MutableLiveData()
    override val newMessages: LiveData<Message> = _newMessages

    override suspend fun sendMessage(chat: Chat,message: Message) {
        repositoryMessanger.sendMessage(chat,message)
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

    override suspend fun getChatsChannels(): Result<List<Chat>> =
        repositoryMessanger.getChats()

    override suspend fun getChatMessages(
        chatId: String
    ): Flow<Message> {
       return repositoryMessanger.getChatMessages(chatId)
    }

    override suspend fun getCurrentUser(onComplete: () -> Unit):Result<User>{
        return repositoryMessanger.getCurrentUser {
            onComplete()
        }
    }

}