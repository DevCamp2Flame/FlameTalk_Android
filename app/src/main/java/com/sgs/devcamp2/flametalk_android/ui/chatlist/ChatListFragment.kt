package com.sgs.devcamp2.flametalk_android.ui.chatlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatListBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ChatListFragment : Fragment() {

    val TAG: String = "로그"

    lateinit var binding: FragmentChatListBinding
    private val model by viewModels<ChatListViewModel>()
    lateinit var adapter: ChatListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatListBinding.inflate(inflater, container, false)
        initUI(this.requireContext())

        model.chattingRoomList.observe(
            this.requireActivity(),
            Observer {

                adapter.submitList(it)
            }
        )
        return binding.root
    }

    private fun initUI(context: Context) {
        binding.rvRoom.layoutManager = LinearLayoutManager(context)
        adapter = ChatListAdapter()
        binding.rvRoom.adapter = adapter
    }
}
