package com.example.comunicator

import java.util.*

class MessageDefault(
    override var time: Date = Date(),
    override val text: String = "",
    override val recipientId: String = "",
    override val senderId: String = "",
    override val senderName: String = ""
) : Message