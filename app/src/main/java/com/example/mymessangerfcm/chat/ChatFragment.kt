package com.example.mymessangerfcm.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.core.comunicator.Message
import com.example.core.domain.logic.core.Chat
import com.example.mymessangerfcm.MessangerViewModel
import com.example.mymessangerfcm.R
import com.example.mymessangerfcm.databinding.FragmentChatBinding
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatFragment : Fragment() {

    lateinit var viewDataBinding: FragmentChatBinding
    val messangerViewModel: MessangerViewModel by sharedViewModel()
    lateinit var listAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentChatBinding.inflate(inflater, container, false).apply {
            viewModel = messangerViewModel
        }
        return viewDataBinding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar_group.load(R.mipmap.ava) {
            transformations(CircleCropTransformation())
        }
        setupListAdapter()
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            messangerViewModel.navigateToChatEvent.value?.peekContent()?.let { chat ->
                messangerViewModel.getChatMessages(
                    chat.id
                ).collect {
                    if (it !in chat.messages){
                        addToChatList(chat, it)
                        scrollToLastMessage()
                    }

                }
            }
        }
    }
}