package com.sgs.devcamp2.flametalk_android.ui.joinuserchatroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentJoinUserChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinUserChatRoomFragment :
    Fragment(),
    JoinUserChatRoomAdapter.ItemClickCallBack,
    View.OnClickListener {

    lateinit var binding: FragmentJoinUserChatRoomBinding
    lateinit var adapter: JoinUserChatRoomAdapter
    private val args by navArgs<JoinUserChatRoomFragmentArgs>()
    private val model by viewModels<JoinUserChatRoomViewModel>()
    val TAG: String = "로그"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinUserChatRoomBinding.inflate(inflater, container, false)

        initUI()
        initObserve()
        return binding.root
    }
    fun initUI() {
        binding.rvJoinUserChatRoom.layoutManager = LinearLayoutManager(context)
        binding.rvJoinUserChatRoomMark.layoutManager = LinearLayoutManager(context)
        adapter = JoinUserChatRoomAdapter(this)
        binding.rvJoinUserChatRoom.adapter = adapter
        binding.layoutJoinUserChatRoomBack.setOnClickListener(this)
        model.getFriendList()
    }

    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.friendListUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            Log.d(TAG, "JoinUserChatRoomFragment - initObserve() called")
                            adapter.submitList(state.data)
                        }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.lastReadMessageId.collect {
                model.joinUser(args.chatroomId, model._userId.value, it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.joinUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            binding.pbLoading.visibility = View.GONE
                            findNavController().popBackStack()
                        }
                }
            }
        }
    }
    override fun onItemClicked(userId: String) {
        binding.pbLoading.visibility = View.VISIBLE
        model.getLastReadMessageId(args.chatroomId, userId)
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.layoutJoinUserChatRoomBack ->
                {
                    findNavController().popBackStack()
                }
        }
    }
}
