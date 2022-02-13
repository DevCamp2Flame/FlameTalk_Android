package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.databinding.ItemOpenChatRoomBinding

/**
 * @author 김현국
 * @created 2022/01/26
 */
class OpenChatRoomAdapter(
    private val context: Context
) : ListAdapter<UserChatRoom, RecyclerView.ViewHolder>(diffUtil) {
    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<UserChatRoom>() {
            override fun areItemsTheSame(oldItem: UserChatRoom, newItem: UserChatRoom): Boolean {
                return oldItem.userChatroomId == newItem.userChatroomId
            }

            override fun areContentsTheSame(oldItem: UserChatRoom, newItem: UserChatRoom): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_open_chat_room, parent, false)

        return OpenChatRoomViewHolder(ItemOpenChatRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OpenChatRoomViewHolder).bind(getItem(position))
    }

    inner class OpenChatRoomViewHolder(val binding: ItemOpenChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserChatRoom) {
            initFriendList(data)
        }

        private fun initFriendList(data: UserChatRoom) {
            Glide.with(itemView).load(data.thumbnail).apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgOpenChatRoom)
//            Glide.with(itemView).load(data.).apply(RequestOptions.circleCropTransform())
//                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
//                .into(binding.imgOpenChatRoomOwner)
            binding.tvOpenChatRoomTitle.text = data.title
            // binding.tvOpenChatRoomDesc.text = data.
            binding.tvOpenChatRoomCount.text = data.count.toString() + "명"
        }
    }
}
