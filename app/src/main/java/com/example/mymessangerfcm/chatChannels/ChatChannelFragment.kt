package com.example.mymessangerfcm.chatChannels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymessangerfcm.MessangerViewModel
import com.example.mymessangerfcm.R
import com.example.mymessangerfcm.chat.EventObserver
import com.example.mymessangerfcm.databinding.FragmentChatChannelBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatChannelFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentChatChannelBinding
    val messangerViewModel: MessangerViewModel by sharedViewModel()

    private lateinit var listAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentChatChannelBinding.inflate(inflater, container, false).apply {
            viewmodel = messangerViewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        messangerViewModel.items.observe(viewLifecycleOwner, Observer {
            it.forEach {
                Log.d("Nurs", "chats ${it.recieverName} ${it.messages}")
            }
        })
        messangerViewModel.navigateToChatEvent.observe(viewLifecycleOwner, EventObserver {
            view.findNavController().navigate(R.id.action_chatChannel_to_chatFragment)
        })
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            viewDataBinding.tasksList.layoutManager = LinearLayoutManager(requireContext())
            listAdapter = TasksAdapter(viewModel)
            viewDataBinding.tasksList.adapter = listAdapter
            viewDataBinding.tasksList.setHasFixedSize(true)
            viewDataBinding.lifecycleOwner = this
        } else {
//            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
        messangerViewModel.items.observe(viewLifecycleOwner, Observer {
            it?.let {

                listAdapter.submitList(it)
            }
        })
    }
}