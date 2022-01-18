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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.DrawerLayoutChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBinding
import com.sgs.devcamp2.flametalk_android.network.response.chat.Chat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 채팅방 리스트에서 한 채팅 클릭 시 이동하게 될 채팅방 내부 fragment
 */
@AndroidEntryPoint
class ChatRoomFragment : Fragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBinding
    lateinit var drawer_bindng: DrawerLayoutChatRoomBinding
    lateinit var adapter: ChatRoomAdapter
    lateinit var userlistAdapter: ChatRoomDrawUserListAdapter
    private val model by viewModels<ChatRoomViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Chat>("chat")?.observe(
            viewLifecycleOwner
        ) {
            result ->
            run {
                Log.d(TAG, "result - $result")
                lifecycleScope.launch {
                    model.addChatting(result)
                }
            }
        }

        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        drawer_bindng = binding.layoutDrawer
        initUI(this.requireContext())

        lifecycleScope.launch {
            model.chatRoom.observe(
                viewLifecycleOwner,
                {
                    Log.d(TAG, "it - $it() called")
                    adapter.submitList(it)
                }
            )
        }

        model.userRoom.observe(
            this.requireActivity(),
            {
                userlistAdapter.submitList(it)
            }
        )

        return binding.root
    }

    fun initUI(context: Context) {
        binding.rvChatRoom.layoutManager = LinearLayoutManager(context)
        drawer_bindng.rvDrawUserList.layoutManager = LinearLayoutManager(context)

        adapter = ChatRoomAdapter()
        userlistAdapter = ChatRoomDrawUserListAdapter()

        binding.rvChatRoom.adapter = adapter
        drawer_bindng.rvDrawUserList.adapter = userlistAdapter

        binding.ivChatRoomDraw.setOnClickListener(this)
        binding.ivChatRoomFile.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatRoomDraw ->
                {
                    Log.d(TAG, "ChatRoomFragment - onClick() called")
                    binding.layoutChatRoomDraw.openDrawer(Gravity.RIGHT)
                }
            binding.ivChatRoomFile ->
                {
                    Log.d(TAG, "ChatRoomFragment - onClick() called")
                    // ChatRoomBottomSheetFragment().show(childFragmentManager, "bottomSheet")
                    findNavController().navigate(R.id.action_navigation_chat_room_to_navigation_chat_Room_Bottom_Sheet)
                }

            binding.ivChatSend ->
                {
                    var chat = Chat(0, "1", "1",)
                    // model.addChatting()
                }
        }
    }
}
