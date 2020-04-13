package com.example.comunicator

import java.util.*

interface Message {
    val time: Date
    val text: String
    val recipientId : String
    val senderId : String
    val senderName : String

}