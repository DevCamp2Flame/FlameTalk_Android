package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.DrawerLayoutChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 채팅방 리스트에서 한 채팅 클릭 시 이동하게 될 채팅방 내부 fragment
 */
@AndroidEntryPoint
class ChatRoomFragment : Fragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBinding
    lateinit var drawer_bindng : DrawerLayoutChatRoomBinding
    lateinit var adapter: ChatRoomAdapter
    lateinit var userlistAdapter: ChatRoomDrawUserListAdapter
    private val model by viewModels<ChatRoomViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        drawer_bindng = binding.layoutDrawer
        initUI(this.requireContext())

        /**
         * viewModel에서 생성한 chat content를 chatRoomAdapter에 전달한다.
         */
        model.chatRoom.observe(
            this.requireActivity(),
            {
                adapter.submitList(it)
            }
        )

        /**
         * viewModel에서 생성한 유저를 user_adpater에 전달한다.
         */
        model.userRoom.observe(
            this.requireActivity(),
            {
                userlistAdapter.submitList(it)
            }
        )

        return binding.root
    }

    /**
     * adapter 두개를 사용하기 위하여 초기화
     */
    fun initUI(context: Context) {
        binding.rvChatRoom.layoutManager = LinearLayoutManager(context)
        drawer_bindng.rvDrawUserList.layoutManager = LinearLayoutManager(context)

        adapter = ChatRoomAdapter()
        userlistAdapter = ChatRoomDrawUserListAdapter()

        binding.rvChatRoom.adapter = adapter
        drawer_bindng.rvDrawUserList.adapter = userlistAdapter

        binding.ivChatRoomDraw.setOnClickListener(this)
    }

    /**
     * 서랍 아이콘 클릭시 drawlayout 을 연다.
     */
    override fun onClick(view: View?) {
        Log.d(TAG, "ChatRoomFragment - view : $view called")
        when (view) {
            binding.ivChatRoomDraw ->
                {
                    Log.d(TAG, "ChatRoomFragment - onClick() called")
                    binding.layoutChatRoomDraw.openDrawer(Gravity.RIGHT)
                }
        }
    }
}
