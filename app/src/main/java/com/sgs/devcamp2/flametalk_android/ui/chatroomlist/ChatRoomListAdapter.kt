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
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.databinding.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/26
 */
class ChatRoomListAdapter @Inject constructor(

    callback: ClickCallBack
) : ListAdapter<ThumbnailWithRoomId, RecyclerView.ViewHolder>(diffUtil) {
    interface ClickCallBack {
        fun onItemLongClicked(position: Int, chatRoom: ThumbnailWithRoomId)
        fun onItemShortClicked(position: Int, chatRoom: ThumbnailWithRoomId)
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
        val diffUtil = object : DiffUtil.ItemCallback<ThumbnailWithRoomId>() {
            override fun areItemsTheSame(oldItem: ThumbnailWithRoomId, newItem: ThumbnailWithRoomId): Boolean {
                return oldItem.room.id == newItem.room.id
            }

            override fun areContentsTheSame(oldItem: ThumbnailWithRoomId, newItem: ThumbnailWithRoomId): Boolean {
                return oldItem.room.count == newItem.room.count && oldItem.thumbnailList.size == newItem.thumbnailList.size &&
                    oldItem.room.title == newItem.room.title && oldItem.room.text == newItem.room.text
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
        when (getItem(position).thumbnailList.size) {
            1 -> {
                (holder as OneViewHolder).bind(getItem(position))
            }
            2 -> {
                (holder as TwoViewHolder).bind(getItem(position))
            }
            3 -> {
                (holder as ThreeViewHolder).bind(getItem(position))
            }
            4 -> {
                (holder as FourViewHolder).bind(getItem(position))
            }
        }
    }
    /**
     * onCreateViewHolder 에서 사용하는 뷰타입을 반환해준다.
     */
    override fun getItemViewType(position: Int): Int {
        return currentList[position].thumbnailList.size
    }
    /**
     * 인원수만큼의 ViewHolder 생성
     * bind 내부에 뷰바인딩을 사용해서 값을 할당해줄 수 있다
     */
    inner class OneViewHolder(val binding: ItemPersonOneChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatroom: ThumbnailWithRoomId) {
            if (chatroom.thumbnailList[0].image == "") {
                Glide.with(binding.ivOneChatRoomListUserImg).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivOneChatRoomListUserImg)
            } else {
                Glide.with(binding.ivOneChatRoomListUserImg).load(chatroom.thumbnailList[0].image)
                    .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivOneChatRoomListUserImg)
            }

            val updated_at = simpleFormat.format(chatroom.room.updated_at)
            if (chatroom.room.messageCount == 0) {
                binding.tvOneChatRoomMessageCount.visibility = View.GONE
            } else {
                binding.tvOneChatRoomMessageCount.visibility = View.VISIBLE
                binding.tvOneChatRoomMessageCount.text = chatroom.room.messageCount.toString()
            }

            binding.tvOneChatRoomListDate.text = updated_at
            binding.tvOneChatRoomListMessage.text = chatroom.room.text
            binding.tvOneChatRoomListUserName.text = chatroom.room.title
        }
    }

    inner class TwoViewHolder(val binding: ItemPersonTwoChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatroom: ThumbnailWithRoomId) {
            if (chatroom.thumbnailList[0].image == "") {
                Glide.with(binding.ivTwoChatRoomListUserImg).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(30)).into(binding.ivTwoChatRoomListUserImg)
            } else {

                Glide.with(binding.ivTwoChatRoomListUserImg).load(chatroom.thumbnailList[0].image)
                    .transform(CenterCrop(), RoundedCorners(30)).into(binding.ivTwoChatRoomListUserImg)
            }
            if (chatroom.thumbnailList[1].image == "") {
                Glide.with(binding.ivTwoChatRoomListUserImg2).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(30)).into(binding.ivTwoChatRoomListUserImg2)
            } else {
                Glide.with(binding.ivTwoChatRoomListUserImg2).load(chatroom.thumbnailList[1].image)
                    .transform(CenterCrop(), RoundedCorners(30)).into(binding.ivTwoChatRoomListUserImg2)
            }
            val updated_at = simpleFormat.format(chatroom.room.updated_at)
            if (chatroom.room.messageCount == 0) {
                binding.tvTwoChatRoomMessageCount.visibility = View.GONE
            } else {
                binding.tvTwoChatRoomMessageCount.visibility = View.VISIBLE
                binding.tvTwoChatRoomMessageCount.text = chatroom.room.messageCount.toString()
            }
            binding.tvTwoChatRoomListDate.text = updated_at
            binding.tvTwoChatRoomListMessage.text = chatroom.room.text
            binding.tvTwoChatRoomListUserName.text = chatroom.room.title
            binding.tvTwoChatRoomListUserCount.text = chatroom.room.count.toString()
        }
    }

    inner class ThreeViewHolder(val binding: ItemPersonThreeChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatroom: ThumbnailWithRoomId) {
            if (chatroom.thumbnailList[0].image == "") {
                Glide.with(binding.ivThreeChatRoomListUserImg)
                    .load(R.drawable.ic_person_white_24).transform(CenterCrop(), RoundedCorners(15))
                    .into(binding.ivThreeChatRoomListUserImg)
            } else {
                Glide.with(binding.ivThreeChatRoomListUserImg)
                    .load(chatroom.thumbnailList[0].image).transform(CenterCrop(), RoundedCorners(15))
                    .into(binding.ivThreeChatRoomListUserImg)
            }
            if (chatroom.thumbnailList[1].image == "") {
                Glide.with(binding.ivThreeChatRoomListUserImg2)
                    .load(R.drawable.ic_person_white_24).transform(CenterCrop(), RoundedCorners(15))
                    .into(binding.ivThreeChatRoomListUserImg2)
            } else {
                Glide.with(binding.ivThreeChatRoomListUserImg2)
                    .load(chatroom.thumbnailList[1].image).transform(CenterCrop(), RoundedCorners(15))
                    .into(binding.ivThreeChatRoomListUserImg2)
            }
            if (chatroom.thumbnailList[2].image == "") {
                Glide.with(binding.ivThreeChatRoomListUserImg3)
                    .load(R.drawable.ic_person_white_24).transform(CenterCrop(), RoundedCorners(15))
                    .into(binding.ivThreeChatRoomListUserImg3)
            } else {
                Glide.with(binding.ivThreeChatRoomListUserImg3)
                    .load(chatroom.thumbnailList[2].image).transform(CenterCrop(), RoundedCorners(15))
                    .into(binding.ivThreeChatRoomListUserImg3)
            }
            if (chatroom.room.messageCount == 0) {
                binding.tvThreeChatRoomMessageCount.visibility = View.GONE
            } else {
                binding.tvThreeChatRoomMessageCount.visibility = View.VISIBLE
                binding.tvThreeChatRoomMessageCount.text = chatroom.room.messageCount.toString()
            }
            val updated_at = simpleFormat.format(chatroom.room.updated_at)
            binding.tvThreeChatRoomListDate.text = updated_at
            binding.tvThreeChatRoomListMessage.text = chatroom.room.text
            binding.tvThreeChatRoomListUserName.text = chatroom.room.title
            binding.tvThreeChatRoomListUserCount.text = chatroom.room.count.toString()
        }
    }

    inner class FourViewHolder(val binding: ItemPersonFourChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatroom: ThumbnailWithRoomId) {
            if (chatroom.thumbnailList[0].image == "") {
                Glide.with(binding.ivFourChatRoomListUserImg).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg)
            } else {
                Glide.with(binding.ivFourChatRoomListUserImg).load(chatroom.thumbnailList[0].image)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg)
            }
            if (chatroom.thumbnailList[1].image == "") {
                Glide.with(binding.ivFourChatRoomListUserImg2).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg2)
            } else {
                Glide.with(binding.ivFourChatRoomListUserImg2).load(chatroom.thumbnailList[1].image)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg2)
            }
            if (chatroom.thumbnailList[2].image == "") {
                Glide.with(binding.ivFourChatRoomListUserImg3).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg3)
            } else {
                Glide.with(binding.ivFourChatRoomListUserImg3).load(chatroom.thumbnailList[2].image)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg3)
            }
            if (chatroom.thumbnailList[3].image == "") {
                Glide.with(binding.ivFourChatRoomListUserImg4).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg4)
            } else {
                Glide.with(binding.ivFourChatRoomListUserImg4).load(chatroom.thumbnailList[3].image)
                    .transform(CenterCrop(), RoundedCorners(5)).into(binding.ivFourChatRoomListUserImg4)
            }
            val updated_at = simpleFormat.format(chatroom.room.updated_at)
            if (chatroom.room.messageCount == 0) {
                binding.tvFourChatRoomMessageCount.visibility = View.GONE
            } else {
                binding.tvFourChatRoomMessageCount.visibility = View.VISIBLE
                binding.tvFourChatRoomMessageCount.text = chatroom.room.messageCount.toString()
            }
            binding.tvFourChatRoomListDate.text = updated_at
            binding.tvFourChatRoomListUserName.text = chatroom.room.title
            binding.tvFourChatRoomListMessage.text = chatroom.room.text
            binding.tvFourChatRoomListUserCount.text = chatroom.room.count.toString()
        }
    }
    /**
     * 각각의 ItemView 에 대해서 LongClick Listener 를 붙여서 클릭시에 dialog callback 을 실행 할 수 있게 한다.
     */
    inner class ItemLongClickListener(var position: Int, var chatroom: ThumbnailWithRoomId) :
        View.OnLongClickListener {
        override fun onLongClick(view: View?): Boolean {
            when (view?.id) {
                R.id.item_one_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, chatroom)
                }
                R.id.item_two_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, chatroom)
                }
                R.id.item_three_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, chatroom)
                }
                R.id.item_four_chat_room_list -> {
                    clickCallBack?.onItemLongClicked(position, chatroom)
                }
            }
            return false
        }
    }

    inner class ItemShortClickListener(var position: Int, var chatroom: ThumbnailWithRoomId) :
        View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.item_one_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, chatroom)
                }
                R.id.item_two_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, chatroom)
                }
                R.id.item_three_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, chatroom)
                }
                R.id.item_four_chat_room_list -> {
                    clickCallBack?.onItemShortClicked(position, chatroom)
                }
            }
        }
    }
}
