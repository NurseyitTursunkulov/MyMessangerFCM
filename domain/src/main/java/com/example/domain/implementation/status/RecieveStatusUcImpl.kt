//package com.example.domain.implementation.status
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Transformations
//import com.example.domain.logic.ChatMessage
//import com.example.domain.logic.User
//import com.example.domain.logic.core.RecieveMessageUseCase
//import com.example.domain.logic.core.RepositoryMessanger
//import com.example.domain.logic.status.Status
//
//class RecieveStatusUcImpl(val repositoryMessanger: RepositoryMessanger):RecieveMessageUseCase {
//    override fun invoke(message: String, user: User): LiveData<Status> {
//        return Transformations.map(repositoryMessanger.getLiveStatus()){
//            it.status
//        }
//    }
//}