package com.example.mymessangerfcm

import android.util.Log
import androidx.lifecycle.*
import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import com.example.mymessangerfcm.chat.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessangerViewModel(val messangerDomainImpl: MessangerDomain) : ViewModel() {

    var messageInputText: String = ""

    val newMessageLiveData: LiveData<Message> = messangerDomainImpl.newMessages

    var currentUser: User? = null

    private val _chatChannelList = MutableLiveData<List<Chat>>().apply { value = emptyList() }
    val chatChannelList: LiveData<List<Chat>> = _chatChannelList

    private val _navigateToChatEvent: MutableLiveData<Event<Chat>> = MutableLiveData()
    val navigateToChatEvent: LiveData<Event<Chat>> = _navigateToChatEvent

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        getCurrentUser({})
        messangerDomainImpl.observeNewMessages()
        viewModelScope.launch {
            _dataLoading.postValue(true)
            withContext(Dispatchers.IO) {
                var result = messangerDomainImpl.getChatsChannels()
                when (result) {
                    is Result.Success -> _chatChannelList.postValue(result.data)
                    is Result.Error -> {
                        /**show error*/
                    }
                }

                _dataLoading.postValue(false)
            }
        }
    }

    fun openChat(chat: Chat) {
        getCurrentUser({})
        _navigateToChatEvent.postValue(Event(chat))
    }

    fun sendMessage(chat: Chat, textMessage: String) {
        if (textMessage.isNotEmpty()) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val message = Message(
                        text = textMessage,
                        recipientId = chat.userIds.first(),
                        senderId = currentUser?.id ?: chat.userIds[1],
                        senderName = currentUser?.name ?: ""
                    )
                    messangerDomainImpl.sendMessage(chat = chat, message = message)
                }
            }
        }
    }

    @InternalCoroutinesApi
    suspend fun getChatMessages(chatId: String): Flow<Message> {
        return messangerDomainImpl.getChatMessages(chatId)
    }


    fun getCurrentUser(onComplete: () -> Unit) {
        viewModelScope.launch {
            val result = messangerDomainImpl.getCurrentUser { onComplete }
            when (result) {
                is Result.Success ->
                    currentUser = result.data
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messangerDomainImpl.unsubscribeFromNewMessages()
    }
}