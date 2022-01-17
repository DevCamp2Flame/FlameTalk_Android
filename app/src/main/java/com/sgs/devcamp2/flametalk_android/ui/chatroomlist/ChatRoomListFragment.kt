package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomListBinding
import com.sgs.devcamp2.flametalk_android.network.response.chatlist.ChatList
import dagger.hilt.android.AndroidEntryPoint

/**
 * 채팅방 리스트 display fragment
 *
 */

@AndroidEntryPoint
class ChatRoomListFragment : Fragment(), ChatRoomListAdapter.ClickCallBack, View.OnClickListener {

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

        /**
         * listAdapter 사용 시 adapter 에서 list type 의 변수를 생성하지 않음
         * submitList 를 통해서 추가해준다.
         */
        model.chatList.observe(
            this.requireActivity(),
            Observer {

                adapterRoom.submitList(it)
            }
        )
        return binding.root
    }

    private fun initUI(context: Context) {
        binding.rvChatListChattingRoom.layoutManager = LinearLayoutManager(context)
        adapterRoom = ChatRoomListAdapter(callback = this)
        binding.rvChatListChattingRoom.adapter = adapterRoom

        binding.ivChatRoomListChat.setOnClickListener(this)
    }

    override fun onItemLongClicked(position: Int, chatList: ChatList) {

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

        dialog.setTitle(chatList.title)
        dialog.setItems(
            items, dialogListener

        )
        dialog.show()
        true
    }

    override fun onItemShortClicked(position: Int, chatList: ChatList) {
        findNavController().navigate(R.id.navigation_chat_room)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatRoomListChat ->
                {
                    ChatRoomTopSheetFragment().show(childFragmentManager, "topsheet")
                }
        }
    }
}
