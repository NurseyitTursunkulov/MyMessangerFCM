package com.example.domain.logic

import com.example.domain.logic.status.Status

interface ChatMessage {
    var sender: User
    var message:String
    var status:Status
}