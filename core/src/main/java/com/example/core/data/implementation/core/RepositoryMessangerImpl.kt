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
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RepositoryMessangerImpl : RepositoryMessanger {

    override lateinit var domain: MessangerDomain

    private val TAG = "Nurs"

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    private val usersCollectionReference = firestoreInstance.collection("users")

    private lateinit var listener: ListenerRegistration

    override fun sendMessage(chat: Chat, message: Message) {
        chatChannelsCollectionRef.document(chat.id).collection("messages")
            .add(message)
    }

    override suspend fun getChats(): Result<List<Chat>> {
        lateinit var result: Result<List<Chat>>
        val chatList = mutableListOf<Chat>()
        chatChannelsCollectionRef.get().addOnSuccessListener { querySnapshot ->
            querySnapshot.forEach { queryDocumentSnapshot ->
                val chat = queryDocumentSnapshot.toObject(Chat::class.java)
                chat.id = queryDocumentSnapshot.id
                chatList.add(chat)
            }
            result = Result.Success(chatList)
        }.addOnFailureListener {
            Log.d("Nurs", "getChats failure $it")
            result = Result.Error(it)
            return@addOnFailureListener
        }.await()

//TODO optimize synchronyc operations!!!!

        chatList.forEach { chat ->
            chatChannelsCollectionRef.document(chat.id).collection("messages").get()
                .addOnSuccessListener { messages ->
                    val messagesList = mutableListOf<Message>()
                    messages.forEach { message ->
                        messagesList.add(message.toObject(Message::class.java))
                    }
                    messagesList.sortBy { it.time }
                    messagesList.forEach {
                        Log.d("messages","${it.time}")
                    }
                    chat.messages.addAll(messagesList)
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
        return result
    }


    @ExperimentalCoroutinesApi
    override suspend fun getChatMessages(
        chatId: String
    ): Flow<Message> = callbackFlow{

        val subscription = chatChannelsCollectionRef .document(chatId).collection("messages").addSnapshotListener{
               snapshot, _ ->
           snapshot?.documentChanges?.forEach {
                when(it.type){
                    DocumentChange.Type.ADDED ->{
                        val newMessage = it.document.toObject(Message::class.java)
                        offer(newMessage)
                        Log.d(TAG, "New city: ${it.document.data}")
                    }
                }
           }
       }
        awaitClose { subscription.remove() }

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