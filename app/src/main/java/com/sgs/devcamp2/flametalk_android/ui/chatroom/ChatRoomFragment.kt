package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.provider.Settings
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
import com.sgs.devcamp2.flametalk_android.databinding.DrawerLayoutChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.ui.MainViewModel
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/01/26
 */
@AndroidEntryPoint
class ChatRoomFragment : Fragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBinding
    lateinit var drawer_binding: DrawerLayoutChatRoomBinding
    lateinit var adapter: ChatRoomAdapter
    lateinit var userlistAdapter: ChatRoomDrawUserListAdapter
    private val model by activityViewModels<ChatRoomViewModel>()
    private val webSocketModel by activityViewModels<MainViewModel>()
    private val args by navArgs<ChatRoomFragmentArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeRoom()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        initUI(this.requireContext())

        binding.etChatRoomInputText.onTextChanged {
            model.updateTextValue(it.toString())
        }

        return binding.root
    }

    fun initUI(context: Context) {
        drawer_binding = binding.layoutDrawer
        binding.rvChatRoom.layoutManager = LinearLayoutManager(context)
        drawer_binding.rvDrawUserList.layoutManager = LinearLayoutManager(context)

        adapter = ChatRoomAdapter(model.userId.value)
        userlistAdapter = ChatRoomDrawUserListAdapter()

        binding.rvChatRoom.adapter = adapter
        drawer_binding.rvDrawUserList.adapter = userlistAdapter

        binding.ivChatRoomDraw.setOnClickListener(this)
        binding.layoutChatRoomArrowSpace.setOnClickListener(this)
        binding.ivChatRoomFile.setOnClickListener(this)
        binding.ivChatSend.setOnClickListener(this)
        drawer_binding.ivDrawExit.setOnClickListener(this)
        drawer_binding.ivDrawSetting.setOnClickListener(this)
        initObserve()
        model.connectWebsocket(
            args.chatroomId,
            Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        )

        getChatList(args.chatroomId)
    }
    /**
     * 채팅 내역 불러오기
     */
    fun getChatList(chatroomId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            model.getChatList(chatroomId)
        }
    }

    /**
     * 채팅방 입장시, 채팅방 구독
     */
    fun subscribeRoom() {
        // session이 연결되어 있는 상태라면, 메세지를 받을 준비를 한다.
        lifecycleScope.launchWhenCreated {
            webSocketModel.session.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        // 내가 보낸 메시지도 받게 되므로 이 순간에 db를 연결한다.
                        model.receivedMessage(state.data, args.chatroomId)
                    }
                }
            }
        }
    }

    fun initObserve() {
        // 타이틀과 채팅방 인원수 init
        viewLifecycleOwner.lifecycleScope.launch {
            model.userChatRoom.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {

                            binding.tvChatRoomTitle.text = state.data.title
                            binding.tvChatRoomUserCount.text = state.data.count.toString()
                        }
                }
            }
        }

        // drawer view에 정보 업데이트
        viewLifecycleOwner.lifecycleScope.launch {
            model.drawUserState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        if (state.data.profileImage == "") {
                            Glide.with(drawer_binding.ivChatRoomDrawUserImage)
                                .load(R.drawable.ic_add_person_blue_24)
                                .into(drawer_binding.ivChatRoomDrawUserImage)
                        } else {
                            Glide.with(drawer_binding.ivChatRoomDrawUserImage)
                                .load(state.data.profileImage)
                                .into(drawer_binding.ivChatRoomDrawUserImage)
                        }
                        drawer_binding.tvChatRoomDrawUser.text = state.data.profileNickname
                        userlistAdapter.submitList(state.data.profiles)
                    }
                    is UiState.Error -> {
                    }
                }
            }
        }
        // db에서 불러온 chat list adapter로 넘기기
        viewLifecycleOwner.lifecycleScope.launch {
            model.chatList.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        // 채팅 데이터 observe
                        Log.d(TAG, "ChatRoomFragment - chatList collect () called")
                        adapter.submitList(state.data)
                        binding.rvChatRoom.smoothScrollToPosition(state.data.size)
                        // model.updateLastReadMessage(state.data[state.data.size - 1].message_id)
                    }
                }
            }
        }
        // drawer view에 채팅방 나가기
        viewLifecycleOwner.lifecycleScope.launch {
            model.deleteUiState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        findNavController().popBackStack()
                    }
                    is UiState.Error -> {
                    }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatRoomDraw -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    model.userChatRoom.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    binding.layoutChatRoomDraw.openDrawer(Gravity.RIGHT)
                                    model.getChatRoomDetail(userChatroomId = state.data.userChatroomId)
                                }
                        }
                    }
                }
            }
            binding.layoutChatRoomArrowSpace ->
                {
                    findNavController().popBackStack()
                }
            binding.ivChatRoomFile -> {
                findNavController().navigate(R.id.action_navigation_chat_room_to_navigation_chat_Room_Bottom_Sheet)
            }
            binding.ivChatSend -> {
                if (model.chat.value != "") {
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    webSocketModel.session.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                model.pushMessage(
                                    "TALK",
                                    args.chatroomId,
                                    state.data,
                                    model.chat.value
                                )
                            }
                        }
                    }
                }
                binding.etChatRoomInputText.setText("")
            }
            drawer_binding.ivDrawExit -> {
                exitDialog()
            }
            drawer_binding.ivDrawSetting -> {
                // thumbnail이 포함된 정보를 넘겨야한다.
                viewLifecycleOwner.lifecycleScope.launch {
                    model.userChatRoom.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    val action =
                                        ChatRoomFragmentDirections.actionNavigationChatRoomToNavigationUpdateChatRoom(
                                            chatroomId = state.data.id
                                        )
                                    findNavController().navigate(action)
                                }
                        }
                    }
                }
            }
        }
    }
    /**
     * 채팅방 나가기 Dialog 출력
     */
    fun exitDialog() {
        val dialog = AlertDialog.Builder(this.requireContext())
        var dialogListener = DialogInterface.OnClickListener { _, which ->
            run {
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                    DialogInterface.BUTTON_POSITIVE -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            model.userChatRoom.collect {
                                state ->
                                when (state) {
                                    is UiState.Success ->
                                        {
                                            model.deleteChatRoom(userChatroomId = state.data.userChatroomId)
                                        }
                                }
                            }
                        }
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



    override fun onStop() {
        super.onStop()
//        lifecycleScope.launch {
//            model.userChatRoom.collect {
//                state ->
//                when (state) {
//                    is UiState.Success ->
//                        {
//                            try {
//                                model.closeChatRoom(
//                                    userChatroomId = state.data.userChatroomId,
//                                    lastReadMessageId = state.data.lastReadMessageId!!
//                                )
//                            } catch (e: Exception) {
//                                Log.d(TAG, "error - $e() called")
//                            }
//                        }
//                }
//            }
//        }
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "ChatRoomFragment - onPause() called")

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "ChatRoomFragment - onStart() called")
        model.updateState()
    }

    override fun onDestroy() {
        super.onDestroy()
        model.saveExitStatus(
            args.chatroomId,
            Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        )
    }
}
