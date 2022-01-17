package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomSelectedBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.Friend

/**
 * @author boris
 * @created 2022/01/13
 */
class InviteRoomSelectedAdapter constructor(
    selectedCallBack: ItemSelectedClickCallBack
) : ListAdapter<Friend, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemSelectedClickCallBack {
        fun onItemSelectedClick(friend: Friend)
    }

    var itemClickCallBack: ItemSelectedClickCallBack? = selectedCallBack

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
            .inflate(R.layout.item_person_invite_room_selected, parent, false)
        return SelectedPersonViewHolder(ItemPersonInviteRoomSelectedBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectedPersonViewHolder).bind(getItem(position))
    }

    inner class SelectedPersonViewHolder(val binding: ItemPersonInviteRoomSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.tvInviteRoomSelectedUserName.text = friend.nickname
            binding.layoutInviteRoomSelected.setOnClickListener(ItemClickListener(friend))
        }
    }

    inner class ItemClickListener(
        var friend: Friend
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_selected -> {
                    itemClickCallBack?.onItemSelectedClick(friend)
                }
            }
        }
    }
}
