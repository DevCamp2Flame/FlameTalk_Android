package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.Friend
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteRoomFragment :
    Fragment(),
    InviteRoomAdapter.ItemClickCallBack,
    InviteRoomMarkAdapter.ItemClickCallBack,
    InviteRoomSelectedAdapter.ItemSelectedClickCallBack,

    View.OnClickListener {
    lateinit var binding: FragmentInviteRoomBinding
    lateinit var roomAdapter: InviteRoomAdapter
    lateinit var roomMarkAdapter: InviteRoomMarkAdapter
    lateinit var inviteRoomSelectedAdapter: InviteRoomSelectedAdapter

    private val model by viewModels<InviteRoomViewModel>()

    val TAG: String = "로그"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviteRoomBinding.inflate(inflater, container, false)

        initUI(this.requireContext())

        model.friendList.observe(
            this.requireActivity(),
            Observer {
                Log.d(TAG, "InviteRoomFragment - friendList :${model.friendList.value}")
                roomAdapter.submitList(it.toMutableList())
                binding.rvInviteRoom.adapter = roomAdapter
            }
        )

        model.friendMarkList.observe(
            this.requireActivity(),
            {
                Log.d(TAG, "InviteRoomFragment - friendMarkList :${model.friendMarkList.value}")
                roomMarkAdapter.submitList(it.toMutableList())
                binding.rvInviteRoomMark.adapter = roomMarkAdapter
            }
        )

        model.inviteFriendList.observe(
            this.requireActivity(),
            {
                if (it.size != 0) {
                    binding.rvInviteRoomSelected.visibility = View.VISIBLE
                    inviteRoomSelectedAdapter.submitList(it)
                    binding.rvInviteRoomSelected.adapter = inviteRoomSelectedAdapter
                } else {
                    binding.rvInviteRoomSelected.visibility = View.GONE
                }
            }
        )

        return binding.root
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
    }

    override fun onItemClicked(friend: Friend, position: Int) {
        model.addMarkFriendToMap(friend, position)
    }

    override fun onItemSelectedClick(friend: Friend, position: Int) {
        model.removeSelectedItem(friend, position)
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.tvInviteRoomSubmit -> {
            }
        }
    }
}
