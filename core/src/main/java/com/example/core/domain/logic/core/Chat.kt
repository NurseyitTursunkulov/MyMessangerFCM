package com.example.core.domain.logic.core

import com.example.core.comunicator.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

data class Chat(
    var id: String = UUID.randomUUID().toString(),
    var recieverName: String = "",
    var userIds: List<String> = emptyList(),
    var messages: MutableList<Message> = mutableListOf()
)
