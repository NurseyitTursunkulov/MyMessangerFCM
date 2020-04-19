package com.example.mymessangerfcm.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mymessangerfcm.FirstFragment
import com.example.mymessangerfcm.chatChannels.ChatChannelsFragment

const val EMPTY_TITLE = "empty_title"
val TAB_TITLES = mapOf(1 to "chats", 2 to "status", 3 to "calls")


class TabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment
        if (position == 1) {
            fragment = ChatChannelsFragment()
        } else {
            fragment = FirstFragment()
            fragment.arguments = Bundle().apply {
                putInt(EMPTY_TITLE, position + 1)
            }
        }
        return fragment
    }
}