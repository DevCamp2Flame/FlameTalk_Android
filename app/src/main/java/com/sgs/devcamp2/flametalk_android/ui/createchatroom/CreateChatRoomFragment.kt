package com.sgs.devcamp2.flametalk_android.ui.createchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sgs.devcamp2.flametalk_android.databinding.FragmentCreateChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.ui.MainViewModel
import com.sgs.devcamp2.flametalk_android.ui.chatroom.ChatRoomViewModel
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import com.sgs.devcamp2.flametalk_android.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/02/08
 */
@AndroidEntryPoint
class CreateChatRoomFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCreateChatRoomBinding
    private val model by viewModels<CreateChatRoomViewModel>()
    private val args by navArgs<CreateChatRoomFragmentArgs>()
    private val webSocketModel by activityViewModels<MainViewModel>()
    private val chatRoomModel by activityViewModels<ChatRoomViewModel>()
    val TAG: String = "로그"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateChatRoomBinding.inflate(inflater, container, false)
        initUI()
        initObserve()
        return binding.root
    }
    fun initUI() {
        binding.etChatRoomInputText.onTextChanged {
            model.updateFirstMessage(it.toString())
        }
        binding.ivChatSend.setOnClickListener(this)
    }
    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.createUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            model.updateRoomInfo(state.data)
                            chatRoomModel.receivedMessage(model.session, model.createdRoomInfo!!.chatroomId)
                            chatRoomModel.pushMessage("INVITE", state.data.chatroomId, model.session, model.firstMessage)
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            chatRoomModel.uiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            if (model.createdRoomInfo != null) {
                                val action = CreateChatRoomFragmentDirections.actionCreateChatRoomFragment2ToNavigationChatRoom(
                                    model.createdRoomInfo!!.chatroomId
                                )
                                findNavController().navigate(action)
                            }
                        }
                }
            }
        }
        subscribeRoom()
    }

    fun subscribeRoom() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // 뷰가 start가 되는 순간 다시 session을 얻는다.
                    webSocketModel.session.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    // 내가 보낸 메시지도 받게 되므로 이 순간에 db를 연결한다.
                                    // sbusribe를 늦게해야한다.
                                    model.session = state.data
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatSend ->
                {
                    // 채팅방 생성 api 호출
                    if (binding.etChatRoomInputText.text.isEmpty()) {
                        this.context?.showToast("메세지를 입력해주세요")
                    } else {
                        model.createChatRoom(args.users.toList())
                    }
                }
        }
    }
}
