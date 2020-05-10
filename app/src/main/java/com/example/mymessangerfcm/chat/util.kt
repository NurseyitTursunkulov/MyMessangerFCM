package com.example.mymessangerfcm.chat

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.comunicator.Message

const val TYPE_SEND = 1
const val TYPE_RECIEVE = 2

class ChatDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}

fun ChatFragment.sentMessage() {
    with(messangerViewModel) {
        navigateToChatEvent.value?.peekContent()?.let { chat ->
            if (messageInputText.isNotEmpty()) {
                val message = com.example.core.comunicator.Message(
                    text = messageInputText,
                    recipientId = chat.userIds.first(),
                    senderId = currentUser?.id ?: chat.userIds[1],
                    senderName = currentUser?.name ?: ""
                )
                sendMessage(
                    chat, message
                )
            }
        }
    }
}

fun ChatFragment.setupListAdapter() {
    viewDataBinding.messagesRv.layoutManager = LinearLayoutManager(requireContext())
    listAdapter = ChatAdapter(messangerViewModel)
    viewDataBinding.messagesRv.adapter = listAdapter
    viewDataBinding.messagesRv.setHasFixedSize(true)
    viewDataBinding.lifecycleOwner = this
    messangerViewModel.navigateToChatEvent.value?.peekContent()?.messages?.let {
        listAdapter.submitList(
            it
        )
        viewDataBinding.messagesRv.apply {
            this.adapter?.itemCount?.let { scrollToPosition(it-1) }
        }
    }
}

fun ChatFragment.scrollToLastMessage() {
    listAdapter.notifyDataSetChanged()
    viewDataBinding.messagesRv.apply {
        this.adapter?.itemCount?.let { scrollToPosition(it - 1) }
    }
}

fun ChatFragment.refreshAdapterItems() {
    listAdapter.submitList(
        messangerViewModel.navigateToChatEvent.value?.peekContent()?.messages
    )
}

fun ChatFragment.getChatId() = messangerViewModel.navigateToChatEvent.value?.peekContent()?.id

fun makeSound(context: Context) {
    try {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, notification)
        r.play()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}