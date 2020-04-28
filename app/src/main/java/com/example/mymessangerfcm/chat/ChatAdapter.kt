package com.example.mymessangerfcm.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.core.comunicator.Message
import com.example.mymessangerfcm.MessangerViewModel


class ChatAdapter(val messangerViewModel: MessangerViewModel) :
    ListAdapter<Message, SealedAdapterViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SealedAdapterViewHolder {
        return if (viewType == TYPE_RECIEVE)
            RecieveMessageViewHolder.from(parent)
        else
            SendMessageViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: SealedAdapterViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is RecieveMessageViewHolder -> holder.bind(item)
            is SendMessageViewHolder -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isMyMessage(position))
            return TYPE_SEND
        else
            return TYPE_RECIEVE
    }

    private fun isMyMessage(position: Int) =
        (getItem(position) as Message).senderId == messangerViewModel.currentUser?.id
}