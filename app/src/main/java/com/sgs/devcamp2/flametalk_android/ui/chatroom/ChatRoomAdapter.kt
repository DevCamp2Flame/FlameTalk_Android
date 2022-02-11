package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.databinding.*
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomAdapter constructor(
    private val host_id: String
) : ListAdapter<Chat, RecyclerView.ViewHolder>(diffUtil) {
    companion object {
        val TAG: String = "로그"
        val simpleFormat = SimpleDateFormat("kk:mm", Locale("ko", "KR"))
        val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.message_id == newItem.message_id
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        /**
         * viewType이 0 이면 오른쪽ViewHolder 에 연결
         * 1이면 왼쪽 View Holder에 연
         */
        return when (viewType) {
            1 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_left_start_text_chat_room, parent, false)
                LeftStartViewHolder(ItemLeftStartTextChatRoomBinding.bind(view))
            }
            0 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_right_start_text_chat_room, parent, false)
                RightStartViewHolder(ItemRightStartTextChatRoomBinding.bind(view))
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_first_invite, parent, false)
                InviteMessageViewHolder(ItemFirstInviteBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /**
         * 0일 일때 오른쪽 ViewHolder
         * 1일때 왼쪽 ViewHolder
         */
        val invite = "INVITE"
        val talk = "TALK"
        when (getItem(position).message_type) {
            invite -> {
            }
            talk -> {
                when (getItem(position).sender_id) {
                    host_id -> {
                        (holder as RightStartViewHolder).bind(getItem(position))
                    }
                    else -> {
                        (holder as LeftStartViewHolder).bind(getItem(position))
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].message_type == "INVITE") {
            2 // 초대 메세지
        } else {
            if (currentList[position].sender_id == host_id) {
                0 // 내 메세지
            } else {
                1 // 상대방 메시지
            }
        }
    }

    inner class InviteMessageViewHolder(val binding: ItemFirstInviteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvItemFirstInviteMessage.text = chat.contents
        }
    }
    /**
     * 내가 아닌 다른 사용자가 보낸 첫번째 메세지 ViewHolder
     */
    inner class LeftStartViewHolder(val binding: ItemLeftStartTextChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvLeftStartTextChatRoomMessage.text = chat.contents
            val date = Date(chat.timeStamp)
            val str_date = simpleFormat.format(date)
            binding.tvLeftStartTextChatRoomListDate.text = str_date
        }
    }
    /**
     * 내가 아닌 다른 사용자가 보낸 첫번째 메세지 이후 연달아 오는 메세지 ViewHolder
     */
    inner class LeftViewHolder(val binding: ItemLeftTextChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvLeftTextChatRoomMessage.text = chat.contents
        }
    }
    /**
     * 내가 보낸 첫번째 메세지 ViewHolder
     */
    inner class RightStartViewHolder(val binding: ItemRightStartTextChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvRightStartTextChatRoomMessage.text = chat.contents
            val date = Date(chat.timeStamp)
            val str_date = simpleFormat.format(date)
            binding.tvRightStartTextChatRoomListDate.text = str_date
        }
    }
    /**
     * 내가 보낸 첫번째 메시지 이후 연달아 전송한 메시지 ViewHolder
     */
    inner class RightViewHolder(val binding: ItemRightTextChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvRightTextChatRoomMessage.text = chat.contents
        }
    }
}
