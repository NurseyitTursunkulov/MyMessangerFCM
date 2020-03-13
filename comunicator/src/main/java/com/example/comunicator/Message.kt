package com.example.comunicator

import java.util.*


object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

interface Message {
    val time: Date
    val text: String
}