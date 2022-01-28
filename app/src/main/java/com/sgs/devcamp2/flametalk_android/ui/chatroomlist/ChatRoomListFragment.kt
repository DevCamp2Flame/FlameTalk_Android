package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomListBinding
import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import com.sgs.devcamp2.flametalk_android.domain.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 채팅방 리스트 display fragment
 *
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
    ): View? {

        binding = FragmentChatRoomListBinding.inflate(inflater, container, false)
        initUI(this.requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            model.uiState.collectLatest {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            adapterRoom.submitList(state.data)
                        }
                }
            }
        }

        return binding.root
    }

    private fun initUI(context: Context) {
        binding.rvChatListChattingRoom.layoutManager = LinearLayoutManager(context)
        adapterRoom = ChatRoomListAdapter(callback = this)
        binding.rvChatListChattingRoom.adapter = adapterRoom
    }

    override fun onItemLongClicked(position: Int, chatRoomList: ChatRoomList) {

        /**
         * itemClickCallback
         * item long Click 시 dialog 생성
         * items 로 메뉴 생성 후 인덱스로 접근
         */

        var items = arrayOf("채팅방 이름 설정", "즐겨찾기에 추가", "채팅방 상단 고정", "채팅방 알람 켜기", "나가기")
        var dialog = AlertDialog.Builder(this.requireContext())
        var dialogListener = DialogInterface.OnClickListener { _, which ->
            run {
                Log.d(TAG, "ChatListFragment - which : $which")
                when (which) {
                    4 ->
                        { Toast.makeText(this.context, "나가기 클릭", Toast.LENGTH_SHORT).show() }
                }
            }
        }

        dialog.setTitle(chatRoomList.title)
        dialog.setItems(
            items, dialogListener

        )
        dialog.show()
        true
    }

    override fun onItemShortClicked(position: Int, chatRoomList: ChatRoomList) {
        findNavController().navigate(R.id.navigation_chat_room)
    }
}
