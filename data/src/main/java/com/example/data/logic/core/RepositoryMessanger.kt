package com.example.data.logic.core

import com.example.comunicator.Message

interface RepositoryMessanger {
    fun sendMessage(message: Message)
}