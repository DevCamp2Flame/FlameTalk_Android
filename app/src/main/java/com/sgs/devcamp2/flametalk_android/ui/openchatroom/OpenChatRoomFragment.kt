package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentOpenChatRoomBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc 프로필 상세 보기 페이지
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class OpenChaRoomFragment : Fragment() {
    private val binding by lazy { FragmentOpenChatRoomBinding.inflate(layoutInflater) }
    private val viewModel: OpenChatRoomViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val openChatRoomAdapter: OpenChatRoomAdapter by lazy {
        OpenChatRoomAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initAppbar()
        initOpenChatRoom()
        initEventListener()
    }

    private fun initAppbar() {
        binding.abOpenChatRoom.tvAppbar.text = "카카오톡오픈채팅"
    }

    private fun initEventListener() {
    }

    private fun initOpenChatRoom() {
        binding.rvOpenChatRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOpenChatRoom.adapter = openChatRoomAdapter

        viewModel.openChatRoom.observe(
            viewLifecycleOwner,
            { it ->
                it?.let {
                    if (it.size > 0) {
                        openChatRoomAdapter.data = it
                        openChatRoomAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }

    private fun initMyOpenChatRoom() {
        // TODO: 나의 오픈 채팅방 리스트
    }

    private fun initMyOpenChatProfile() {
        // TODO: 나의 오픈 프로필 리스트
    }
}
