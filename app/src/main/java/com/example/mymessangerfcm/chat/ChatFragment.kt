package com.example.mymessangerfcm.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.mymessangerfcm.MessangerViewModel
import com.example.mymessangerfcm.R
import com.example.mymessangerfcm.databinding.FragmentChatBinding
import com.example.mymessangerfcm.databinding.FragmentChatChannelBinding
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentChatBinding
    val messangerViewModel: MessangerViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentChatBinding.inflate(inflater, container, false).apply {
            viewModel = messangerViewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar_group.load(R.mipmap.ava){
            transformations(CircleCropTransformation())
        }
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}