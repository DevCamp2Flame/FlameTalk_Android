package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.network.response.friend.Friend
import com.sgs.devcamp2.flametalk_android.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InviteRoomFragment :
    Fragment(),
    InviteRoomAdapter.ItemClickCallBack,
    InviteRoomMarkAdapter.ItemMarkClickCallBack,
    InviteRoomSelectedAdapter.ItemSelectedClickCallBack,

    View.OnClickListener {
    lateinit var binding: FragmentInviteRoomBinding
    lateinit var roomAdapter: InviteRoomAdapter
    lateinit var roomMarkAdapter: InviteRoomMarkAdapter
    lateinit var inviteRoomSelectedAdapter: InviteRoomSelectedAdapter

    private val model by activityViewModels<InviteRoomViewModel>()

    val TAG: String = "로그"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviteRoomBinding.inflate(inflater, container, false)

        initUI(this.requireContext())
        initObserve2()

        viewLifecycleOwner.lifecycleScope.launch {
            model.friendList.collect {
                roomAdapter.submitList(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.markFriendList.collect {
                roomMarkAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            model.selectedFriendList.collect {
                if (it.isNotEmpty()) {
                    binding.rvInviteRoomSelected.visibility = View.VISIBLE
                } else {
                    binding.rvInviteRoomSelected.visibility = View.GONE
                }
                inviteRoomSelectedAdapter.submitList(it)
            }
        }

        return binding.root
    }
    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.uiEvent.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    // state가 계속 유지되는듯. 따라서 뒤로가기를 눌러도 안돌아감.
                                    // 이슈
                                    Log.d(TAG, "UiState - $state() called")
                                    findNavController().navigate(R.id.navigation_chat_room)
                                }
                        }
                    }
                }
            }
        }
    }

    fun initObserve2() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.uiState.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    findNavController().navigate(R.id.navigation_chat_room)
                                }
                            is UiState.Error ->
                                {
                                    //Toast.makeText(,state.error,Toast.LENGTH_SHORT).show
                                    requireActivity().showToast(state.error)
                                }
                        }
                    }
                }
            }
        }
    }

    fun initUI(context: Context) {
        binding.rvInviteRoom.layoutManager = LinearLayoutManager(context)
        binding.rvInviteRoomMark.layoutManager = LinearLayoutManager(context)
        binding.rvInviteRoomSelected.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        roomAdapter = InviteRoomAdapter(this)
        roomMarkAdapter = InviteRoomMarkAdapter(this)
        inviteRoomSelectedAdapter = InviteRoomSelectedAdapter(this)

        binding.rvInviteRoom.adapter = roomAdapter
        binding.rvInviteRoomMark.adapter = roomMarkAdapter
        binding.rvInviteRoomSelected.adapter = inviteRoomSelectedAdapter

        binding.tvInviteRoomSubmit.setOnClickListener(this)
    }

    override fun onItemClicked(friend: Friend, position: Int, adapter: InviteRoomAdapter) {
        model.addFriendList(friend, position, adapter)
    }
    override fun onItemMarkClicked(friend: Friend, position: Int, adapter: InviteRoomMarkAdapter) {
        model.addMarkList(friend, position, adapter)
    }
    override fun onItemSelectedClick(friend: Friend) {
        model.removeSelectedItem(friend)
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.tvInviteRoomSubmit -> {
                model.createRooms()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"InviteRoomFragment - onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"InviteRoomFragment - onStop() called")
    }
}
