package com.example.mymessangerfcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.domain.implementation.MessangerDomainImpl
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.User
import com.example.mymessangerfcm.chat.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessangerViewModel(val messangerDomainImpl: MessangerDomainImpl) : ViewModel() {

    var messageInputText: String = ""

    val newMessageLiveData: LiveData<Message> = messangerDomainImpl.newMessages

    var currentUser: User? = null

    private val _items = MutableLiveData<List<Chat>>().apply { value = emptyList() }
    val items: LiveData<List<Chat>> = _items

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
                var getChatList: List<Chat> = messangerDomainImpl.getChatsChannel()
                _items.postValue(getChatList)
                _dataLoading.postValue(false)
            }
        }
    }

    fun openChat(chat: Chat) {
        getCurrentUser({})
        _navigateToChatEvent.postValue(Event(chat))
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                messangerDomainImpl.sendMessage(message = message)
            }
        }
    }

    @InternalCoroutinesApi
    fun getChatMessages(chatId: String) {
        viewModelScope.launch {
            messangerDomainImpl.getChatMessages(chatId).collect {

            }
        }
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