package com.example.mymessangerfcm.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.mymessangerfcm.MessangerViewModel
import com.example.mymessangerfcm.R
import com.example.mymessangerfcm.databinding.FragmentChatBinding
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.InternalCoroutinesApi
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
        messangerViewModel.playSoundEvent.observe(viewLifecycleOwner, EventObserver {
            makeSound(requireContext())
        })
        messangerViewModel.newMessageLiveData.observe(viewLifecycleOwner, Observer {
            refreshAdapterItems()
            scrollToLastMessage()
        })
        getChatId()?.let { chatId ->
            messangerViewModel.observeChatForNewMessages(chatId)
        }
    }
}