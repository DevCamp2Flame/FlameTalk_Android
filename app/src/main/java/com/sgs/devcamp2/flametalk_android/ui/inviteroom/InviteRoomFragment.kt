package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom.FriendEntity
import com.sgs.devcamp2.flametalk_android.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author 김현국
 * @created 2022/01/12
 */
@AndroidEntryPoint
class InviteRoomFragment :
    Fragment(),
    InviteRoomAdapter.ItemClickCallBack,
    InviteRoomSelectedAdapter.ItemSelectedClickCallBack,

    View.OnClickListener {
    lateinit var binding: FragmentInviteRoomBinding
    lateinit var roomAdapter: InviteRoomAdapter
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
        initObserve()

        viewLifecycleOwner.lifecycleScope.launch {
            model.friendList.collect {
                roomAdapter.submitList(it)
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
                    model.uiState.collect {
                        state ->
                        when (state) {
                            is UiState.Success ->
                                {
                                    findNavController().navigate(R.id.action_inviteRoomFragment_to_navigation_chat_room_Fragment)
                                }
                            is UiState.Error ->
                                {
                                    requireActivity().showToast(state.error)
                                }
                            is UiState.Loading ->
                                {
                                }
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.friendListUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            roomAdapter.submitList(state.data)
                        }
                }
            }
        }
    }

    fun initUI(context: Context) {
        binding.rvInviteRoom.layoutManager = LinearLayoutManager(context)
        binding.rvInviteRoomSelected.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        roomAdapter = InviteRoomAdapter(this)
        inviteRoomSelectedAdapter = InviteRoomSelectedAdapter(this)

        binding.rvInviteRoom.adapter = roomAdapter
        binding.rvInviteRoomSelected.adapter = inviteRoomSelectedAdapter

        binding.tvInviteRoomSubmit.setOnClickListener(this)
        binding.layoutInviteRoomBack.setOnClickListener(this)
        model.getFriendList()
    }

    override fun onItemClicked(friendEntity: FriendEntity, position: Int, adapter: InviteRoomAdapter) {
        model.addFriendList(friendEntity, position, adapter)
    }
    override fun onItemSelectedClick(tempFriend: FriendEntity) {
        model.removeSelectedItem(tempFriend)
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.tvInviteRoomSubmit -> {
                var users: MutableList<String> = emptyList<String>().toMutableList()
                for (i in 0 until model.selectedFriendList.value.size) {
                    users.add(model.selectedFriendList.value[i].userId)
                }
                val action = InviteRoomFragmentDirections.actionNavigationInviteRoomToCreateChatRoomFragment2(users.toTypedArray())
                findNavController().navigate(action)
            }
            binding.layoutInviteRoomBack ->
                {
                    findNavController().popBackStack()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        model.replaceUiState()
    }
}
