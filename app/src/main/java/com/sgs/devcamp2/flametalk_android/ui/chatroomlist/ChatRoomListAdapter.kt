package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.databinding.*
import java.text.SimpleDateFormat
import java.util.*

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
    val TAG: String = "로그"

    companion object {
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd kk:mm", Locale("ko", "KR"))
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
                return oldItem.userChatroomId == newItem.userChatroomId
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
        when (getItem(position).thumbnail.size) {
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
        return currentList[position].thumbnail.size
    }
    /**
     * 인원수만큼의 ViewHolder 생성
     * bind 내부에 뷰바인딩을 사용해서 값을 할당해줄 수 있다
     */
    inner class OneViewHolder(val binding: ItemPersonOneChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            Glide.with(binding.ivOneChatRoomListUserImg).load(userChatRoom.thumbnail[0])
                .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivOneChatRoomListUserImg)
            binding.tvOneChatRoomListUserName.text = userChatRoom.title
            binding.tvOneChatRoomListUserCount.text = userChatRoom.count.toString()
        }
    }

    inner class TwoViewHolder(val binding: ItemPersonTwoChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            Glide.with(binding.ivTwoChatRoomListUserImg).load(userChatRoom.thumbnail[0])
                .transform(CenterCrop(), RoundedCorners(30)).into(binding.ivTwoChatRoomListUserImg)
            Glide.with(binding.ivTwoChatRoomListUserImg2).load(userChatRoom.thumbnail[1])
                .transform(CenterCrop(), RoundedCorners(30)).into(binding.ivTwoChatRoomListUserImg2)
            binding.tvTwoChatRoomListUserName.text = userChatRoom.title
            binding.tvTwoChatRoomListUserCount.text = userChatRoom.count.toString()
        }
    }

    inner class ThreeViewHolder(val binding: ItemPersonThreeChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            Glide.with(binding.ivThreeChatRoomListUserImg)
                .load(userChatRoom.thumbnail[0]).transform(CenterCrop(), RoundedCorners(15))
                .into(binding.ivThreeChatRoomListUserImg)
            Glide.with(binding.ivThreeChatRoomListUserImg2).load(userChatRoom.thumbnail[1])
                .transform(CenterCrop(), RoundedCorners(15))
                .into(binding.ivThreeChatRoomListUserImg2)
            Glide.with(binding.ivThreeChatRoomListUserImg3).load(userChatRoom.thumbnail[2])
                .transform(CenterCrop(), RoundedCorners(15))
                .into(binding.ivThreeChatRoomListUserImg3)
            binding.tvThreeChatRoomListUserName.text = userChatRoom.title
            binding.tvThreeChatRoomListUserCount.text = userChatRoom.count.toString()
        }
    }

    inner class FourViewHolder(val binding: ItemPersonFourChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userChatRoom: UserChatRoom) {
            Glide.with(binding.ivFourChatRoomListUserImg).load(userChatRoom.thumbnail[0])
                .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg)
            Glide.with(binding.ivFourChatRoomListUserImg2).load(userChatRoom.thumbnail[1])
                .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg2)
            Glide.with(binding.ivFourChatRoomListUserImg3).load(userChatRoom.thumbnail[2])
                .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg3)
            Glide.with(binding.ivFourChatRoomListUserImg4).load(userChatRoom.thumbnail[3])
                .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg4)
            binding.tvFourChatRoomListUserName.text = userChatRoom.title
            binding.tvFourChatRoomListUserCount.text = userChatRoom.count.toString()
        }
    }
    /**
     * 각각의 ItemView 에 대해서 LongClick Listener 를 붙여서 클릭시에 dialog callback 을 실행 할 수 있게 한다.
     */
    inner class ItemLongClickListener(var position: Int, var userChatRoom: UserChatRoom) :
        View.OnLongClickListener {
        override fun onLongClick(view: View?): Boolean {
            when (view?.id) {
                R.id.item_one_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, userChatRoom)
                }
                R.id.item_two_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, userChatRoom)
                }
                R.id.item_three_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, userChatRoom)
                }
                R.id.item_four_chat_room_list -> {
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
                R.id.item_one_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, userChatRoom)
                }
                R.id.item_two_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, userChatRoom)
                }
                R.id.item_three_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, userChatRoom)
                }
                R.id.item_four_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, userChatRoom)
                }
            }
        }
    }
}
