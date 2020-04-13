package com.example.core.data.implementation.core

import android.util.Log
import com.example.comunicator.Message
import com.example.comunicator.MessageDefault
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class RepositoryMessangerImpl : RepositoryMessanger {

    override lateinit var domain: MessangerDomain

    private val TAG = "Nurs"

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    //TODO method must recieve parameter of chat
    private val messageCollectionReference = firestoreInstance.collection("message")

    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    private val usersCollectionReference = firestoreInstance.collection("users")

    private lateinit var listener: ListenerRegistration

    override fun sendMessage(message: Message) {
        messageCollectionReference
            .add(message)
    }

    override fun subscribeForNewMessages() {
        listener = messageCollectionReference
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

    override suspend fun getChats(): List<Chat> {
        val chatList = mutableListOf<Chat>()
        chatChannelsCollectionRef.get().addOnSuccessListener {
            it.forEach {
                val chat = it.toObject(Chat::class.java)
                chatList.add(chat)
            }
        }.addOnFailureListener {
            Log.d("Nurs", "getChats failure $it")
        }.await()

        chatList.forEach { chat ->
            usersCollectionReference.document(chat.userIds.first()).get().addOnSuccessListener {
                it.data?.get("name")?.let {
                    chat.recieverName = it as String
                }
            }.await()
        }
        Log.d("Nurs", "chats = $chatList")
        return chatList
    }
}