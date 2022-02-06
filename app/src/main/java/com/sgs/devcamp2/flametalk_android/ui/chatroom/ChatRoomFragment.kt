package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.DrawerLayoutChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.ui.MainActivityViewModel
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
    private val model by activityViewModels<ChatRoomViewModel>()
    private val webSocketModel by activityViewModels<MainActivityViewModel>()
    private val args by navArgs<ChatRoomFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        initUI(this.requireContext())
        lifecycleScope.launch {
            model.chatRoom.collect {
                adapter.submitList(it) {
                    binding.rvChatRoom.smoothScrollToPosition(it.size)
                }
            }
        }

        model.userRoom.observe(
            this.requireActivity()
        ) {
            userlistAdapter.submitList(it)
        }
        binding.etChatRoomInputText.onTextChanged {
            model.updateTextValue(it.toString())
        }

        return binding.root
    }

    fun initUI(context: Context) {
        drawer_bindng = binding.layoutDrawer
        binding.rvChatRoom.layoutManager = LinearLayoutManager(context)
        drawer_bindng.rvDrawUserList.layoutManager = LinearLayoutManager(context)

        adapter = ChatRoomAdapter()
        userlistAdapter = ChatRoomDrawUserListAdapter()

        binding.rvChatRoom.adapter = adapter
        drawer_bindng.rvDrawUserList.adapter = userlistAdapter

        binding.ivChatRoomDraw.setOnClickListener(this)
        binding.ivChatRoomFile.setOnClickListener(this)
        binding.ivChatSend.setOnClickListener(this)

        subscribeRoom()
    }

    fun subscribeRoom() {
        viewLifecycleOwner.lifecycleScope.launch {
            webSocketModel.session.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            model.receivedMessage(state.data, args.roomId)
                        }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatRoomDraw -> {
                binding.layoutChatRoomDraw.openDrawer(Gravity.RIGHT)
            }
            binding.ivChatRoomFile -> {
                findNavController().navigate(R.id.action_navigation_chat_room_to_navigation_chat_Room_Bottom_Sheet)
            }
            binding.ivChatSend -> {
                model.initRoom(args.roomId)
                viewLifecycleOwner.lifecycleScope.launch {
                    webSocketModel.session.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    model.pushMessage(args.roomId, state.data, model.chat.value)
                                }
                        }
                    }
                }
                binding.etChatRoomInputText.setText("")
            }
        }
    }
}
