package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBinding
    private val model by viewModels<ChatRoomViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    fun initUI() {
    }
}
