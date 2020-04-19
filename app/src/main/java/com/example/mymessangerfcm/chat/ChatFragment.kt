package com.example.mymessangerfcm.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import coil.transform.CircleCropTransformation
import com.example.mymessangerfcm.R
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatar_group.load(R.drawable.ic_launcher_foreground){
            transformations(CircleCropTransformation())
        }
    }
}