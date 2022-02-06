package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentMyOpenChatRoomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOpenChatRoomFragment : Fragment() {

    lateinit var binding: FragmentMyOpenChatRoomBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOpenChatRoomBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }
    fun initUI() {
        binding.rvMyOpenChatRoom.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}
