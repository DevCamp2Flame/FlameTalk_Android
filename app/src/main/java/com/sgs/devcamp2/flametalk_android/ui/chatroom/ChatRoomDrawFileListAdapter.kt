package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemChatRoomDrawFileBinding

/**
 * @author boris
 * @created 2022/02/18
 */
class ChatRoomDrawFileListAdapter :
    ListAdapter<String, RecyclerView.ViewHolder>(diffUtil) {
    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
                return true
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
                return true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room_draw_file, parent, false)
        return DrawFileListViewHolder(ItemChatRoomDrawFileBinding.bind(view))
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DrawFileListViewHolder).bind(getItem(position))
    }
    inner class DrawFileListViewHolder(val binding: ItemChatRoomDrawFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            Log.d(TAG, "url - $str")
            Glide.with(binding.ivChatRoomDrawFile).load(str)
                .into(binding.ivChatRoomDrawFile)
        }
    }
}
