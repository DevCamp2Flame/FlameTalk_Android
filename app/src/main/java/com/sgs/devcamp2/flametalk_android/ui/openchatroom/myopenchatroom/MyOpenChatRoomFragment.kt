package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentMyOpenChatRoomBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOpenChatRoomFragment : Fragment() {

    lateinit var binding: FragmentMyOpenChatRoomBinding
    lateinit var myOpenChatroomAdapter: MyOpenChatRoomAdapter
    private val viewModel: MyOpenChatRoomViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOpenChatRoomBinding.inflate(inflater, container, false)

        initUI()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myOpenChatRoom.observe(
                viewLifecycleOwner
            ) {
                myOpenChatroomAdapter.data = it
            }
        }
        return binding.root
    }
    fun initUI() {
        binding.rvMyOpenChatRoom.layoutManager = LinearLayoutManager(context)

        myOpenChatroomAdapter = MyOpenChatRoomAdapter(this.requireContext())

        binding.rvMyOpenChatRoom.adapter = myOpenChatroomAdapter
    }
}
