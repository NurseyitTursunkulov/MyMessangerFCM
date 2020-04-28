package com.example.mymessangerfcm.chat

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.comunicator.Message

const val TYPE_SEND = 1
const val TYPE_RECIEVE = 2

class TaskDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
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
    }
}