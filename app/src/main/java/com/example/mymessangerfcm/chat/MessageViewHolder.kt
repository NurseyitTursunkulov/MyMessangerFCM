package com.example.mymessangerfcm.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.comunicator.Message
import com.example.mymessangerfcm.databinding.ItemMessageReceiveBinding
import com.example.mymessangerfcm.databinding.ItemMessageSendBinding

sealed class SealedAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

data class RecieveMessageViewHolder(val binding: ItemMessageReceiveBinding) :
    SealedAdapterViewHolder(binding.root) {

    fun bind(item: Message) {
        binding.message = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SealedAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMessageReceiveBinding.inflate(layoutInflater, parent, false)

            return RecieveMessageViewHolder(binding)
        }
    }
}

data class SendMessageViewHolder(val binding: ItemMessageSendBinding) :
    SealedAdapterViewHolder(binding.root) {
    fun bind(item: Message) {
        binding.message = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SealedAdapterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMessageSendBinding.inflate(layoutInflater, parent, false)

            return SendMessageViewHolder(binding)
        }
    }
}
