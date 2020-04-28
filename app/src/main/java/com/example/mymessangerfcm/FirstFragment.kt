package com.example.mymessangerfcm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    val messangerViewModel: MessangerViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendButton.setOnClickListener {
//            val messageToSend =
//                text = editTextTextPersonName.text.toString(),
//            time = Calendar.getInstance().time
//            )
//            messangerViewModel.sendMessage(message
//                MessageDefault(ToSend)
            val action =
                FirstFragmentDirections.actionFirstFragmentToHomeFragment()
            findNavController().navigate(R.id.action_Global_to_SecondFragment)
        }
        messangerViewModel.newMessageLiveData.observe(requireActivity(),Observer{
            Log.d("Nurs","new message in fragment ${it.text}")
        })
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            val action =
                FirstFragmentDirections.actionFirstFragmentToSecondFragment("From FirstFragment")
            findNavController().navigate(action)
        }
    }
}
