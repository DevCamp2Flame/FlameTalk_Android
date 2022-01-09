package com.sgs.devcamp2.flametalk_android.ui.chatlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemChattingRoomListFourBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemChattingRoomListOneBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemChattingRoomListThreeBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemChattingRoomListTwoBinding
import com.sgs.devcamp2.flametalk_android.domain.model.response.chatlist.ChattingRoom

class ChatListAdapter : ListAdapter<ChattingRoom, RecyclerView.ViewHolder>(diffUtil) {
// 클래스에서만 한번만 쓸려고하는 것

    companion object {
        private const val ONEPERSON = 1
        private const val TWOPERSON = 2
        private const val THREEPERSON = 3
        private const val FOURPERSON = 4

        val diffUtil = object : DiffUtil.ItemCallback<ChattingRoom>() {
            override fun areItemsTheSame(oldItem: ChattingRoom, newItem: ChattingRoom): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChattingRoom, newItem: ChattingRoom): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val view: View?
        return when (viewType) {
            ONEPERSON -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting_room_list_one, parent, false)
                OneViewHolder(ItemChattingRoomListOneBinding.bind(view))
            }
            TWOPERSON -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting_room_list_two, parent, false)
                TwoViewHolder(ItemChattingRoomListTwoBinding.bind(view))
            }
            THREEPERSON -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting_room_list_three, parent, false)
                ThreeViewHolder(ItemChattingRoomListThreeBinding.bind(view))
            }
            else ->
                {
                    view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting_room_list_four, parent, false)
                    FourViewHolder(ItemChattingRoomListFourBinding.bind(view))
                }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItem(position).user_list.size != 0) {

            when (getItem(position).user_list.size) {
                1 ->
                    {
                        (holder as OneViewHolder).bind(getItem(position))
                    }
                2 ->
                    {
                        (holder as TwoViewHolder).bind(getItem(position))
                    }
                3 ->
                    {
                        (holder as ThreeViewHolder).bind(getItem(position))
                    }
                else ->
                    {
                        (holder as FourViewHolder).bind(getItem(position))
                    }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return currentList[position].user_list.size
    }

    // inner
    inner class OneViewHolder(val binding: ItemChattingRoomListOneBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chattingRoom: ChattingRoom) {
            binding.tvUserName.text = chattingRoom.title
        }
    }

    inner class TwoViewHolder(val binding: ItemChattingRoomListTwoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chattingRoom: ChattingRoom) {
            binding.tvUserName.text = chattingRoom.title
        }
    }
    inner class ThreeViewHolder(val binding: ItemChattingRoomListThreeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chattingRoom: ChattingRoom) {
            binding.tvUserName.text = chattingRoom.title
        }
    }
    inner class FourViewHolder(val binding: ItemChattingRoomListFourBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chattingRoom: ChattingRoom) {
            binding.tvUserName.text = chattingRoom.title

        }
    }
}
