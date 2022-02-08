package com.sgs.devcamp2.flametalk_android.ui.createchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sgs.devcamp2.flametalk_android.databinding.FragmentCreateChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import kotlinx.coroutines.launch

class CreateChatRoomFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentCreateChatRoomBinding
    private val model by viewModels<CreateChatRoomViewModel>()
    private val args by navArgs<CreateChatRoomFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateChatRoomBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }
    fun initUI() {
        binding.etChatRoomInputText.onTextChanged {
            model.updateFirstMessage(it.toString())
        }
    }
    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.createUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            // 채팅방이 성공적으로 생성이 되었다면 메세지를 전송
                            model.pushFirstMessage()
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
              //      model.createChatRoom(args.users.toList())
                }
        }
    }
}
