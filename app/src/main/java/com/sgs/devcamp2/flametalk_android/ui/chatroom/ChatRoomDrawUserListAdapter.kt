package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.Profile
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonChattingRoomBinding

/**
 * @author 김현국
 * @created 2022/01/26
 * 채팅방 오른쪽 drawlayout에서 사용되는 user list recyclerview의 adpter
 * 유저리스트의 정보들을 recyclerview에 보여주기위한 adpater
 */
class ChatRoomDrawUserListAdapter :
    ListAdapter<Profile, RecyclerView.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Profile>() {
            override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
                return oldItem.id == newItem.id
                return true
            }

            override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
                return oldItem == newItem
                return true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person_chatting_room, parent, false)
        return ChattingRoomUserViewHolder(ItemPersonChattingRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChattingRoomUserViewHolder).bind(getItem(position))
    }

    inner class ChattingRoomUserViewHolder(val binding: ItemPersonChattingRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: Profile) {
            binding.tvChatRoomDrawUser.text = profile.nickname
            if (profile.image != null) {
                Glide.with(this.binding.ivChatRoomDrawUserImage)
                    .load(profile.image).into(binding.ivChatRoomDrawUserImage)
            } else {
                Glide.with(this.binding.ivChatRoomDrawUserImage)
                    .load(R.drawable.ic_add_person_blue_24).into(binding.ivChatRoomDrawUserImage)
            }
        }
    }
}
