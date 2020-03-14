package com.example.core.data.implementation.core

import com.example.comunicator.Message
import com.example.core.data.logic.core.RepositoryMessanger
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryMessangerImpl : RepositoryMessanger {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val chatChannelsCollectionRef = firestoreInstance.collection("message")

    override fun sendMessage(message: Message) {
        // Add a new document with a generated id.
        chatChannelsCollectionRef
            .add(message)
    }
}