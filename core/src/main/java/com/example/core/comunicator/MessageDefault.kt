package com.example.comunicator

import java.util.*

class MessageDefault(
    override var time: Date = Date(),
    override val text: String = ""
) : Message {
}