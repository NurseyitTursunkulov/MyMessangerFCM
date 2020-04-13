package com.example.mymessangerfcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comunicator.Message
import com.example.core.domain.implementation.MessangerDomainImpl
import com.example.core.domain.logic.core.Chat
import com.example.mymessangerfcm.chat.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessangerViewModel(val messangerDomainImpl: MessangerDomainImpl) : ViewModel() {

    val newMessageLiveData: LiveData<Message> = messangerDomainImpl.newMessages
    private val _items = MutableLiveData<List<Chat>>().apply { value = emptyList() }
    val items: LiveData<List<Chat>> = _items

    private val _navigateToChatEvent: MutableLiveData<Event<Chat>> = MutableLiveData()
    val navigateToChatEvent: LiveData<Event<Chat>> = _navigateToChatEvent

    init {
        messangerDomainImpl.observeNewMessages()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var h: List<Chat> = messangerDomainImpl.getChats()
                _items.postValue(h)
            }
        }
    }

    fun openChat(chat: Chat) {
        _navigateToChatEvent.postValue(Event(chat))
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                messangerDomainImpl.sendMessage(message = message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messangerDomainImpl.unsubscribeFromNewMessages()
    }
}