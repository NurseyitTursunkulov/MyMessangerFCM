package com.example.core.data.implementation.core

import android.util.Log
import com.example.comunicator.Message
import com.example.comunicator.MessageDefault
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.logic.core.MessangerDomain
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class RepositoryMessangerImpl : RepositoryMessanger {

    override lateinit var domain: MessangerDomain

    private val TAG = "Nurs"

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val chatChannelsCollectionRef = firestoreInstance.collection("message")

    private lateinit var listener: ListenerRegistration

    override fun sendMessage(message: Message) {
        chatChannelsCollectionRef
            .add(message)
    }

    override fun subscribeForNewMessages() {
        listener = chatChannelsCollectionRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.documentChanges?.forEach { dc ->
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            domain.onNewMessageRecieved(dc.document.toObject(MessageDefault::class.java))
                            Log.d(TAG, "New message: ${dc.document.data}")
                        }
                    }
                }
            }
    }

    override fun unsubscribe() {
        listener.remove()
    }
}