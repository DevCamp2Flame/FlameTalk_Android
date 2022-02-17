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
import com.sgs.devcamp2.flametalk_android.network.response.friend.FriendListRes
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
        initObserve()

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
        binding.layoutInviteRoomBack.setOnClickListener(this)
    }

    override fun onItemClicked(tempFriend: FriendListRes, position: Int, adapter: InviteRoomAdapter) {
        model.addFriendList(tempFriend, position, adapter)
    }
    override fun onItemMarkClicked(tempFriend: FriendListRes, position: Int, adapter: InviteRoomMarkAdapter) {
        model.addMarkList(tempFriend, position, adapter)
    }
    override fun onItemSelectedClick(tempFriend: FriendListRes) {
        model.removeSelectedItem(tempFriend)
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.tvInviteRoomSubmit -> {
                var users: MutableList<String> = emptyList<String>().toMutableList()
                for (i in 0 until model.selectedFriendList.value.size) {
                    users.add(model.selectedFriendList.value[i].nickname)
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
