package com.example.mymessangerfcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comunicator.Message
import com.example.core.domain.implementation.MessangerDomainImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessangerViewModel(val messangerDomainImpl: MessangerDomainImpl) : ViewModel() {

    val newMessageLiveData: LiveData<Message> = messangerDomainImpl.newMessages

    init {
        messangerDomainImpl.observeNewMessages()
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