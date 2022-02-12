package com.sgs.devcamp2.flametalk_android.ui.createopenchatroom

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.data.model.User

/**
 * @author boris
 * @created 2022/02/02
 */
class CreateOpenChatRoomAdapter : ListAdapter<User, RecyclerView.ViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                TODO("Not yet implemented")
            }

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
