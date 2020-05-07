package com.example.core.domain.implementation.util

import com.example.core.comunicator.Message

fun isNewMessage(
    newMsg: Message,
    messageList: MutableList<Message>
): Boolean = newMsg !in messageList