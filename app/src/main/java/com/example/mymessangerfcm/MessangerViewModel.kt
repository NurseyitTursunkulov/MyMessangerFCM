package com.example.mymessangerfcm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comunicator.Message
import com.example.domain.implementation.core.MessangerDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessangerViewModel(val messangerDomain : MessangerDomain) : ViewModel() {

    fun sendMessage(message: Message) {
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                messangerDomain.sendMessage(message = message)
            }
        }

    }
}