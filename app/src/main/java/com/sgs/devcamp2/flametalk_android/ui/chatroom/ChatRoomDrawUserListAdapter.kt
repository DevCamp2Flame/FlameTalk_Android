package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonChattingRoomBinding

/**
 * 채팅방 오른쪽 drawlayout에서 사용되는 user list recyclerview의 adpter
 * 유저리스트의 정보들을 recyclerview에 보여주기위한 adpater
 */
class ChatRoomDrawUserListAdapter constructor() : ListAdapter<String, RecyclerView.ViewHolder>(diffUtil) {

    /**
     * diffUtil을 정의 했지만, 더미데이터를 사용하고 있기 때문에 현재는 사용을 하고 있지 않다.
     */
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                //   return oldItem.id == newItem.id
                return true
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                //     return oldItem == newItem
                return true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person_chatting_room, parent, false)
        return ChattingRoomUserViewHolder(ItemPersonChattingRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChattingRoomUserViewHolder).bind(getItem(position))
    }

    /**
     * 후에 user 객체를 itemView에 바인딩하기 위한 ViewHolder
     */
    inner class ChattingRoomUserViewHolder(val binding: ItemPersonChattingRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            binding.tvChatRoomDrawUser.text = str
        }
    }
}