package com.example.mymessangerfcm

import androidx.lifecycle.*
import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import com.example.mymessangerfcm.chat.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessangerViewModel(val messangerDomainImpl: MessangerDomain) : ViewModel() {

    var messageInputText: String = ""

    var currentUser: User? = null

    private val _chatChannelList = MutableLiveData<List<Chat>>().apply { value = emptyList() }
    val chatChannelList: LiveData<List<Chat>> = _chatChannelList

    private val _navigateToChatEvent: MutableLiveData<Event<Chat>> = MutableLiveData()
    val navigateToChatEvent: LiveData<Event<Chat>> = _navigateToChatEvent

    private val _playSoundEvent: MutableLiveData<Event<Unit>> = MutableLiveData()
    val playSoundEvent: LiveData<Event<Unit>> = _playSoundEvent

    private val _newMessageLiveData: MutableLiveData<Message> = MutableLiveData()
    val newMessageLiveData: LiveData<Message> = _newMessageLiveData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        launchCoroutineOnDispatcherIO {
            _dataLoading.postValue(true)
            val result = messangerDomainImpl.getChatsChannels()
            when (result) {
                is Result.Success -> _chatChannelList.postValue(result.data)
                is Result.Error -> {
                    /**show error*/
                }
            }

            _dataLoading.postValue(false)
        }
    }

    fun openChat(chat: Chat) {
        getCurrentUser({})
        _navigateToChatEvent.postValue(Event(chat))
        messangerDomainImpl.currentChat = chat
    }

    fun sendMessage(chat: Chat, message: Message) {
        launchCoroutineOnDispatcherIO {
            messangerDomainImpl.sendMessage(chat = chat, message = message)
        }
    }

    @InternalCoroutinesApi
    fun observeChatForNewMessages(chatId: String) {
        launchCoroutineOnDispatcherIO {
            messangerDomainImpl.getChatMessagesFlow(chatId)
                .collect { newMsg ->
                    getCurrentChat()?.let { chat ->
                        displayNewMessage(newMsg, chat)
                    }
                }
        }
    }


    fun getCurrentUser(onComplete: () -> Unit) {
        launchCoroutineOnDispatcherIO {
            val result: Result<User> = messangerDomainImpl.getCurrentUser { onComplete }
            when (result) {
                is Result.Success ->
                    currentUser = result.data
            }
        }
    }

    private fun displayNewMessage(
        newMsg: Message,
        chat: Chat
    ) {
        chat.messages.add(newMsg)
        _newMessageLiveData.postValue(newMsg)
        if (isNotMyMessage(newMsg))
            _playSoundEvent.postValue(Event(Unit))

    }

    private fun getCurrentChat(): Chat? = _navigateToChatEvent.value?.peekContent()

}