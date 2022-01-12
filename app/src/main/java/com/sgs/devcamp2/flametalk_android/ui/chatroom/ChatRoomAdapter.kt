package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemLeftStartTextChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemLeftTextChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemRightStartTextChatRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemRightTextChatRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.model.response.chat.Chat

class ChatRoomAdapter constructor() : ListAdapter<Chat, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id
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
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_left_start_text_chat_room, parent, false)
                LeftStartViewHolder(ItemLeftStartTextChatRoomBinding.bind(view))
            }
            else ->
                {
                    view = LayoutInflater.from(parent.context).inflate(R.layout.item_right_start_text_chat_room, parent, false)
                    RightStartViewHolder(ItemRightStartTextChatRoomBinding.bind(view))
                }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
/**
         * 0일 일때 오른쪽 ViewHolder
         * 1일때 왼쪽 ViewHolder
         */
        when (getItem(position).user_id) {
            "0" ->
                {
                    (holder as RightStartViewHolder).bind(getItem(position))
                }
            "1" ->
                {

                    (holder as LeftStartViewHolder).bind(getItem(position))
                }
        }
    }

    override fun getItemViewType(position: Int): Int {
        // return currentList[position].user_id

        /**
         * user_id = String "0" 일때 Int 0 반환
         * 그외 1 반환
         */
        return if (currentList[position].user_id == "0") {
            0
        } else {
            1
        }
    }

    /**
     * 내가 아닌 다른 사용자가 보낸 첫번째 메세지 ViewHolder
     */
    inner class LeftStartViewHolder(val binding: ItemLeftStartTextChatRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvChatRoomMessage.text = chat.content
        }
    }

    /**
     * 내가 아닌 다른 사용자가 보낸 첫번째 메세지 이후 연달아 오는 메세지 ViewHolder
     */
    inner class LeftViewHolder(val binding: ItemLeftTextChatRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvChatRoomMessage.text = chat.content
        }
    }

    /**
     * 내가 보낸 첫번째 메세지 ViewHolder
     */
    inner class RightStartViewHolder(val binding: ItemRightStartTextChatRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvChatRoomMessage.text = chat.content
        }
    }

    /**
     * 내가 보낸 첫번째 메시지 이후 연달아 전송한 메시지 ViewHolder
     */
    inner class RightViewHolder(val binding: ItemRightTextChatRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvChatRoomMessage.text = chat.content
        }
    }
}
