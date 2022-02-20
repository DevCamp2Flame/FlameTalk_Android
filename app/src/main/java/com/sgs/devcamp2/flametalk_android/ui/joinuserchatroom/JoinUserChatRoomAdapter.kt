package com.sgs.devcamp2.flametalk_android.ui.joinuserchatroom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.inviteroom.FriendListRes
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonJoinRoomBinding

/**
 * @author 김현국
 * @created 2022/02/17
 */
class JoinUserChatRoomAdapter(callback: JoinUserChatRoomFragment) :
    ListAdapter<FriendListRes, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemClickCallBack {
        fun onItemClicked(userId: String)
    }

    val itemClickCallBack = callback

    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<FriendListRes>() {
            override fun areItemsTheSame(oldItem: FriendListRes, newItem: FriendListRes): Boolean {
                return oldItem.friendId == newItem.friendId
            }

            override fun areContentsTheSame(
                oldItem: FriendListRes,
                newItem: FriendListRes
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person_join_room, parent, false)
        return PersonViewHolder(ItemPersonJoinRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PersonViewHolder).bind(getItem(position))
    }

    inner class PersonViewHolder(val binding: ItemPersonJoinRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendEntity: FriendListRes) {
            Log.d(TAG, "friendList - (${friendEntity.nickname}) called")
            if (friendEntity.preview.imageUrl == "") {
                Glide.with(binding.ivJoinRoomImage).load(R.drawable.ic_person_white_24).into(binding.ivJoinRoomImage)
            } else {
                Glide.with(binding.ivJoinRoomImage).load(friendEntity.preview.imageUrl).into(binding.ivJoinRoomImage)
            }
            binding.tvJoinRoomUserName.text = friendEntity.nickname
            binding.layoutJoinRoomItem.setOnClickListener(ItemClickListener(friendEntity.userId))
        }
    }

    inner class ItemClickListener(
        var userId: String
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_join_room_item -> {
                    itemClickCallBack?.onItemClicked(userId)
                }
            }
        }
    }
}
