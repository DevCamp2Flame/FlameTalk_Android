package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.Friend

/**
 * @author boris
 * @created 2022/01/12
 */
class InviteRoomAdapter(callback: InviteRoomFragment) :
    ListAdapter<Friend, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemClickCallBack {
        fun onItemClicked(friend: Friend, position: Int, adapter: InviteRoomAdapter)
    }

    val itemClickCallBack = callback

    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.selected == newItem.selected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person_invite_room, parent, false)
        return PersonViewHolder(ItemPersonInviteRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PersonViewHolder).bind(getItem(position), position)
    }

    inner class PersonViewHolder(val binding: ItemPersonInviteRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend, position: Int) {

            binding.ivInviteRoomRadioButton.isActivated = friend.selected != "0"
            binding.layoutInviteRoomItem.setOnClickListener(
                ItemClickListener(friend, position, binding)
            )
            binding.tvInviteRoomUserName.text = friend.nickname
        }
    }

    inner class ItemClickListener(
        var friend: Friend,
        var position: Int,
        var binding: ItemPersonInviteRoomBinding

    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_item -> {
                    if (binding.ivInviteRoomRadioButton.isActivated) {
                        Log.d(TAG, "binding.ivInviteRoomRadioButton.isSelected - ${binding.ivInviteRoomRadioButton.isSelected} called")
                        binding.ivInviteRoomRadioButton.isActivated = false
                    } else {
                        binding.ivInviteRoomRadioButton.isActivated = true
                    }
                    itemClickCallBack?.onItemClicked(friend, position, this@InviteRoomAdapter)
                }
            }
        }
    }
    fun putActivate(position: Int) {
        var list: MutableList<Friend> = currentList.toMutableList()
        var friend: Friend = currentList[position].copy()
        friend.selected = "1"
        list[position] = friend
        submitList(list)
    }
    fun removeActivate(position: Int) {
        var list: MutableList<Friend> = currentList.toMutableList()
        var friend: Friend = currentList[position].copy()
        friend.selected = "0"
        list[position] = friend
        submitList(list)
    }
}
