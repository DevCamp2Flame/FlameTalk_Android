package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
import com.bumptech.glide.Glide
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import com.sgs.devcamp2.flametalk_android.databinding.DrawerLayoutChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.ui.MainActivityViewModel
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 채팅방 리스트에서 한 채팅 클릭 시 이동하게 될 채팅방 내부 fragment
 */
@AndroidEntryPoint
class ChatRoomFragment : Fragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBinding
    lateinit var drawer_binding: DrawerLayoutChatRoomBinding
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
        binding.etChatRoomInputText.onTextChanged {
            model.updateTextValue(it.toString())
        }

        return binding.root
    }

    fun initUI(context: Context) {
        drawer_binding = binding.layoutDrawer
        binding.rvChatRoom.layoutManager = LinearLayoutManager(context)
        drawer_binding.rvDrawUserList.layoutManager = LinearLayoutManager(context)

        adapter = ChatRoomAdapter()
        userlistAdapter = ChatRoomDrawUserListAdapter()

        binding.rvChatRoom.adapter = adapter
        drawer_binding.rvDrawUserList.adapter = userlistAdapter

        binding.ivChatRoomDraw.setOnClickListener(this)
        binding.ivChatRoomFile.setOnClickListener(this)
        binding.ivChatSend.setOnClickListener(this)
        drawer_binding.ivDrawExit.setOnClickListener(this)
        drawer_binding.ivDrawSetting.setOnClickListener(this)
        initObserve()
        model.getChatList(args.chatroomId)
    }

    fun subscribeRoom() {
        lifecycleScope.launch {
            webSocketModel.session.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            // 내가 보낸 메시지도 받게 되므로 이 순간에 db를 연결한다.
                            model.receivedMessage(state.data, args.chatroomId)
                        }
                }
            }
        }
    }
    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.drawUserState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            Glide.with(drawer_binding.ivChatRoomDrawUserImage).load(state.data.profileImage).into(drawer_binding.ivChatRoomDrawUserImage)
                            drawer_binding.tvChatRoomDrawUser.text = state.data.profileNickname
                            userlistAdapter.submitList(state.data.profiles)
                        }
                    is UiState.Error ->
                        {
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.chatList.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            // 채팅 데이터 observe
                            Log.d(TAG, "ChatRoomFragment - chatList collect () called")
                            adapter.submitList(state.data)
                            model.updateLastReadMessage(state.data[state.data.size - 1].message_id)
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.deleteUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            findNavController().popBackStack()
                        }
                    is UiState.Error ->
                        {
                        }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatRoomDraw -> {
                binding.layoutChatRoomDraw.openDrawer(Gravity.RIGHT)
                model.getChatRoomDetail(userChatroomId = args.userChatroomId)
            }
            binding.ivChatRoomFile -> {
                findNavController().navigate(R.id.action_navigation_chat_room_to_navigation_chat_Room_Bottom_Sheet)
            }
            binding.ivChatSend -> {
                model.initRoom(args.chatroomId)

                // 후에 서버로 올릴떄
//                viewLifecycleOwner.lifecycleScope.launch {
//                    webSocketModel.session.collect {
//                        state ->
//                        when (state) {
//                            is UiState.Success ->
//                                {
                // 서버에 보내고 반환이 된다면 디비에 저장한다.
//                                    model.pushMessage(args.chatroomId, state.data, model.chat.value)
//                                }
//                        }
//                    }
//                }
                if (model.chat.value != "") {
                    val chatEntity = ChatEntity("talk", "1643986912282658350", args.chatroomId, model.chat.value)
                    model.pushMessage(chatEntity)
                }

                binding.etChatRoomInputText.setText("")
            }
            drawer_binding.ivDrawExit ->
                {
                    exitDialog()
                }
            drawer_binding.ivDrawSetting ->
                {
                    val action = ChatRoomFragmentDirections.actionNavigationChatRoomToUpdateChatRoomFragment(args.userChatroomId, args.userChatRoom)
                    findNavController().navigate(action)
                }
        }
    }

    fun exitDialog() {
        val dialog = AlertDialog.Builder(this.requireContext())
        var dialogListener = DialogInterface.OnClickListener { _, which ->
            run {
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE ->
                        {
                        }
                    DialogInterface.BUTTON_POSITIVE ->
                        {
                            model.deleteChatRoom(userChatroomId = args.userChatroomId)
                        }
                }
            }
        }
        dialog.setTitle("채팅방 나가기")
        dialog.setMessage("채팅방에서 나가시겠습니까? 나가기를 하면 대화내용이 모두 삭제되고, 채팅목록에서도 삭제됩니다.")
        dialog.setPositiveButton("확인", dialogListener)
        dialog.setNegativeButton("취소", dialogListener)
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   testLifeCycle()
        // 구독 유지
        subscribeRoom()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "ChatRoomFragment - onPause() called")
        model.closeChatRoom(userChatroomId = args.userChatroomId, lastReadMessageId = model.lastReadMessageId.value)
    }
}
