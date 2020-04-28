package com.example.core.data.implementation.core

import android.util.Log
import com.example.core.comunicator.Message
import com.example.core.comunicator.Result
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.logic.core.Chat
import com.example.core.domain.logic.core.MessangerDomain
import com.example.core.domain.logic.core.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
                            domain.onNewMessageRecieved(dc.document.toObject(Message::class.java))
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
        chatChannelsCollectionRef.get().addOnSuccessListener { querySnapshot ->
            querySnapshot.forEach { queryDocumentSnapshot ->
                val chat = queryDocumentSnapshot.toObject(Chat::class.java)
                chat.id = queryDocumentSnapshot.id
                chatList.add(chat)
            }
        }.addOnFailureListener {
            Log.d("Nurs", "getChats failure $it")
        }.await()

//TODO optimize synchronyc operations!!!!

        chatList.forEach { chat ->
            chatChannelsCollectionRef.document(chat.id).collection("messages").get()
                .addOnSuccessListener { messages ->
                    messages.forEach { message ->
                        chat.messages.add(message.toObject(Message::class.java))
                    }
                }
                .addOnFailureListener {
                    Log.d("Nurs", "oshibka messages")
                }.await()
        }

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


    @ExperimentalCoroutinesApi
    override suspend fun getChatMessages(chatId: String): Flow<Message> = flow {
        val eventDocument = chatChannelsCollectionRef
            .document(chatId)
        val subscription = eventDocument.addSnapshotListener { snapshot, _ ->
            if (snapshot!!.exists()) {
                val versionCode = snapshot.toObject(Message::class.java)
                versionCode?.let {
                    GlobalScope.launch {
                        emit(it)
                    }
                    Log.d("Nurs", "new flow ${it.text}")
                }
            }

        }
    }

    private val currentUserDocRef: DocumentReference
        get() = FirebaseFirestore.getInstance().document(
            "users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")}"
        )

    override suspend fun getCurrentUser(onComplete: () -> Unit): Result<User> {
        lateinit var result: Result<User>
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    id = documentSnapshot.id,
                    name = FirebaseAuth.getInstance().currentUser?.displayName ?: "fff"
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }

                result = Result.Success(newUser)
            } else {
                val user = documentSnapshot.toObject(User::class.java)
                user?.id = documentSnapshot.id
                user?.let {
                    result = Result.Success(it)
                }
                onComplete()
            }
        }.addOnFailureListener {
            result = Result.Error(Exception("failure accessing firebase"))
        }.await()
        return result
    }
}