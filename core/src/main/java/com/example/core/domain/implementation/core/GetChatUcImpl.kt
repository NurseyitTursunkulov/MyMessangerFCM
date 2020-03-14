//package com.example.domain.implementation.core
//
//import com.example.domain.logic.ChatMessage
//import com.example.domain.logic.User
//import com.example.domain.logic.core.GetChatUseСase
//import com.example.domain.logic.core.RepositoryMessanger
//
//class GetChatUcImpl(val repository: RepositoryMessanger) : GetChatUseСase {
//    override fun invoke(sender: User, reciever: User): List<ChatMessage> {
//        return repository.getChat(sender,reciever)
//    }
//}