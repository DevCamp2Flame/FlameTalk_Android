package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomListBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.ui.chattingviewpager.ChattingViewPagerFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/01/26
 */
@AndroidEntryPoint
class ChatRoomListFragment : Fragment(), ChatRoomListAdapter.ClickCallBack {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomListBinding
    private val model by viewModels<ChatRoomListViewModel>()
    lateinit var adapterRoom: ChatRoomListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomListBinding.inflate(inflater, container, false)
        initUI(this.requireContext())
        initObserve()
        return binding.root
    }

    private fun initUI(context: Context) {
        binding.rvChatListChattingRoom.layoutManager = LinearLayoutManager(context)
        adapterRoom = ChatRoomListAdapter(callback = this)
        binding.rvChatListChattingRoom.itemAnimator = null
        binding.rvChatListChattingRoom.itemAnimator = DefaultItemAnimator()

        binding.rvChatListChattingRoom.adapter = adapterRoom
        model.getChatRoomList(false)
        model.getDeviceToken(this.requireContext())
    }

    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.uiState.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                model.getLocalChatRoomList(false)
                            }
                            is UiState.Error ->
                                {
                                    model.getLocalChatRoomList(false)
                                }
                            is UiState.Loading ->
                                {
                                }
                            is UiState.Init ->
                                {
                                }
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.localUiState.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                if (state.data.isNotEmpty()) {
                                    val list: List<ThumbnailWithRoomId> = state.data.sortedByDescending {
                                        it.room.updated_at
                                    }
                                    adapterRoom.submitList(list)
                                }
                            }
                            is UiState.Error ->
                                {
                                }
                            is UiState.Loading ->
                                {
                                }
                            is UiState.Init ->
                                {
                                }
                        }
                    }
                }
            }
        }
        pushDeviceToken()
        successSavedToken()
    }

    override fun onItemLongClicked(position: Int, chatRoom: ThumbnailWithRoomId) {
        /**
         * itemClickCallback
         * item long Click 시 dialog 생성
         * items 로 메뉴 생성 후 인덱스로 접근
         */
        val items = arrayOf("채팅방 이름 설정", "즐겨찾기에 추가", "채팅방 상단 고정", "채팅방 알람 켜기", "나가기")
        val dialog = AlertDialog.Builder(this.requireContext())
        val dialogListener = DialogInterface.OnClickListener { _, which ->
            run {
                when (which) {
                    4 -> {
                        Toast.makeText(this.context, "나가기 클릭", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        dialog.setTitle(chatRoom.room.title)
        dialog.setItems(
            items, dialogListener
        )
        dialog.show()
    }

    override fun onItemShortClicked(position: Int, chatRoom: ThumbnailWithRoomId) {
        val action =
            ChattingViewPagerFragmentDirections.actionNavigationChattingViewPagerFragmentToNavigationChatRoom(
                chatRoom.room.id,
            )
        findNavController().navigate(action)
    }

    private fun pushDeviceToken() {
        lifecycleScope.launch {
            model.deviceToken.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        model.saveDeviceToken(state.data)
                    }
                    is UiState.Error ->
                        {
                        }
                    is UiState.Init ->
                        {
                        }
                    is UiState.Loading ->
                        {
                        }
                }
            }
        }
    }

    private fun successSavedToken() {
        lifecycleScope.launch {
            model.deviceTokenUiState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        context?.showToast("토큰 저장 성공")
                    }
                    else -> {
                        // Log.d(TAG, "successSavedToken - error() called")
                        context?.showToast("토큰 저장 실패")
                    }
                }
            }
        }
    }
}
