package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.databinding.*

/**
 * 채팅방 리스트 recyclerview Adapter
 * @constructor callback 은 아이템 클릭 콜백입니다
 *
 *
 */
class ChatRoomListAdapter constructor(
    callback: ClickCallBack
) : ListAdapter<UserChatRoom, RecyclerView.ViewHolder>(diffUtil) {
// 클래스에서만 한번만 쓸려고하는 것
    interface ClickCallBack {
        fun onItemLongClicked(position: Int, userChatRoom: UserChatRoom)
        fun onItemShortClicked(position: Int, userChatRoom: UserChatRoom)
    }

    var clickCallBack: ClickCallBack? = callback

    companion object {
        private const val ONEPERSON = 1
        private const val TWOPERSON = 2
        private const val THREEPERSON = 3
        private const val FOURPERSON = 4
        /**
         * notifyDataSetChanged 를 사용하지 않기 위한 방법
         * DiffUtil 를 사용하여 다른 부분만 갱신을 해준다.
         */
        val diffUtil = object : DiffUtil.ItemCallback<UserChatRoom>() {
            override fun areItemsTheSame(oldItem: UserChatRoom, newItem: UserChatRoom): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserChatRoom, newItem: UserChatRoom): Boolean {
                return oldItem == newItem
            }
        }
    }
    /**
     * viewType 으로 2명 , 3명 ,4명이상 단톡방인지 구분한다.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            ONEPERSON -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_person_one_chat_list, parent, false)
                OneViewHolder(ItemPersonOneChatListBinding.bind(view))
            }
            TWOPERSON -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_person_two_chat_list, parent, false)
                TwoViewHolder(ItemPersonTwoChatListBinding.bind(view))
            }
            THREEPERSON -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_person_three_chat_list, parent, false)
                ThreeViewHolder(ItemPersonThreeChatListBinding.bind(view))
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_person_four_chat_list, parent, false)
                FourViewHolder(ItemPersonFourChatListBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener(ItemLongClickListener(position, getItem(position)))
        holder.itemView.setOnClickListener(ItemShortClickListener(position, getItem(position)))
        when (getItem(position).count) {
            1 -> {
                (holder as OneViewHolder).bind(getItem(position))
            }
            2 -> {
                (holder as TwoViewHolder).bind(getItem(position))
            }
            3 -> {
                (holder as ThreeViewHolder).bind(getItem(position))
            }
            else -> {
                (holder as FourViewHolder).bind(getItem(position))
            }
        }
    }
    /**
     * onCreateViewHolder 에서 사용하는 뷰타입을 반환해준다.
     */
    override fun getItemViewType(position: Int): Int {
        return currentList[position].count
    }
    /**
     * 인원수만큼의 ViewHolder 생성
     * bind 내부에 뷰바인딩을 사용해서 값을 할당해줄 수 있다
     */
    inner class OneViewHolder(val binding: ItemPersonOneChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            binding.tvChatRoomListUserName.text = userChatRoom.title
        }
    }

    inner class TwoViewHolder(val binding: ItemPersonTwoChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            binding.tvChatRoomListUserName.text = userChatRoom.title
        }
    }

    inner class ThreeViewHolder(val binding: ItemPersonThreeChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            binding.tvChatRoomListUserName.text = userChatRoom.title
        }
    }

    inner class FourViewHolder(val binding: ItemPersonFourChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            binding.tvChatRoomListUserName.text = userChatRoom.title
        }
    }
    /**
     * 각각의 ItemView 에 대해서 LongClick Listener 를 붙여서 클릭시에 dialog callback 을 실행 할 수 있게 한다.
     */
    inner class ItemLongClickListener(var position: Int, var userChatRoom: UserChatRoom) :
        View.OnLongClickListener {
        override fun onLongClick(view: View?): Boolean {
            when (view?.id) {
                R.id.item_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, userChatRoom)
                }
            }
            return false
        }
    }

    inner class ItemShortClickListener(var position: Int, var userChatRoom: UserChatRoom) :
        View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.item_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, userChatRoom)
                }
            }
        }
    }
}
