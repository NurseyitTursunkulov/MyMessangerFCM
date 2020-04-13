package com.example.core.domain.logic.core

import com.example.comunicator.Message
import java.util.*

data class Chat(
    var id: String = UUID.randomUUID().toString(),
    var recieverName: String = "",
    var userIds: List<String> = emptyList(),
    var messages: List<Message> = emptyList()
)
