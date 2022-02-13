package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.OpenProfile
import com.sgs.devcamp2.flametalk_android.databinding.ItemMyOpenChatProfileBinding

/**
 * @author 김현국
 * @created 2022/01/26
 */
class MyOpenChatProfileAdapter(callback: MyOpenChatProfileFragment) : ListAdapter<OpenProfile, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemClickCallBack {
        fun onItemClicked(openProfile: OpenProfile)
    }

    val itemClickCallBack = callback
    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<OpenProfile>() {
            override fun areItemsTheSame(oldItem: OpenProfile, newItem: OpenProfile): Boolean {
                return oldItem.openProfileId == newItem.openProfileId
            }

            override fun areContentsTheSame(oldItem: OpenProfile, newItem: OpenProfile): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatProfileHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_open_chat_profile, parent, false)
        return OpenChatProfileHolder(ItemMyOpenChatProfileBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OpenChatProfileHolder).bind(getItem(position))
    }

    inner class OpenChatProfileHolder(val binding: ItemMyOpenChatProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(openProfile: OpenProfile) {
            binding.itemChatRoomList.setOnClickListener(ItemClickListener(openProfile))
            binding.tvMyOpenChatProfileNickname.text = openProfile.nickname
            Glide.with(this.itemView).load(openProfile.imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgMyOpenChatProfile)
        }
    }

    inner class ItemClickListener(
        val openProfile: OpenProfile,
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.item_chat_room_list ->
                    {
                        itemClickCallBack?.onItemClicked(openProfile)
                    }
            }
        }
    }
}
