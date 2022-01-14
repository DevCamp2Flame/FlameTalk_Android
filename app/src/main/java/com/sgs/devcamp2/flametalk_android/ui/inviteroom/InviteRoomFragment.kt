package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
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
    InviteRoomMarkAdapter.ItemClickCallBack,
    InviteRoomAdapter.ItemClickCallBack,
    InviteRoomSelectedAdapter.ItemClickCallBack,
    Adapter.ClickCallBack,
    View.OnClickListener {
    lateinit var binding: FragmentInviteRoomBinding
//    lateinit var roomMarkAdapter: InviteRoomMarkAdapter
//    lateinit var roomAdapter: InviteRoomAdapter
//    lateinit var inviteRoomSelectedAdapter: InviteRoomSelectedAdapter

    lateinit var adapter: Adapter
    private val model by viewModels<InviteRoomViewModel>()

    val TAG: String = "로그"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInviteRoomBinding.inflate(inflater, container, false)

        initUI(this.requireContext())

//        model.friendMarkList.observe(
//            this.requireActivity(),
//            Observer {
//                roomMarkAdapter.submitList(it)
//            }
//        )
//
        model.friendList.observe(
            this.requireActivity(),
            Observer {
                adapter.submitList(it)
                Log.d(TAG,"InviteRoomFragment - submit() called")
            }
        )
//
//        model.inviteFriendList.observe(
//            this.requireActivity(),
//            Observer {
//                if (it.size != 0) {
//                    binding.rvInviteRoomSelected.visibility = View.VISIBLE
//                    Log.d(TAG, "InviteRoomFragment - MutableList<Friend> $it")
//                    inviteRoomSelectedAdapter.submitList(it?.toMutableList())
//                } else {
//                    binding.rvInviteRoomSelected.visibility = View.GONE
//                }
//            }
//        )

        return binding.root
    }

    fun initUI(context: Context) {
        binding.rvInviteRoomMark.layoutManager = LinearLayoutManager(context)
        binding.rvInviteRoom.layoutManager = LinearLayoutManager(context)
        binding.rvInviteRoomSelected.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        roomMarkAdapter = InviteRoomMarkAdapter(markCallback = this)
//        roomAdapter = InviteRoomAdapter(callback = this)
//        inviteRoomSelectedAdapter = InviteRoomSelectedAdapter(selectedCallBack = this)

        adapter = Adapter(callback = this)

//        binding.rvInviteRoomMark.adapter = roomMarkAdapter
//        binding.rvInviteRoom.adapter = roomAdapter
//        binding.rvInviteRoomSelected.adapter = inviteRoomSelectedAdapter

        binding.rvInviteRoom.adapter = adapter

        binding.rvInviteRoomMark.adapter = adapter

        binding.rvInviteRoomSelected.adapter = adapter
    }

    override fun onItemClicked(friend: Friend) {
        model.addMarkFriendToMap(friend)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvInviteRoomSubmit -> {
            }
        }
    }
}
