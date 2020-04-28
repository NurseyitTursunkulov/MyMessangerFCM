package com.example.core.comunicator

import java.util.*

data class Message(
    var id:String ="",
    var time: Date = Date(),
    val text: String = "",
    val recipientId: String = "",
    val senderId: String = "",
    val senderName: String = ""
)