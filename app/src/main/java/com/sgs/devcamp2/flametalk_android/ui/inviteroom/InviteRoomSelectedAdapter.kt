package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
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
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomSelectedBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom.FriendEntity
import com.sgs.devcamp2.flametalk_android.util.disableClickTemporarily

/**
 * @author 김현국
 * @created 2022/01/13
 */
class InviteRoomSelectedAdapter constructor(
    selectedCallBack: ItemSelectedClickCallBack
) : ListAdapter<FriendEntity, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemSelectedClickCallBack {
        fun onItemSelectedClick(tempFriend: FriendEntity)
    }

    var itemClickCallBack: ItemSelectedClickCallBack? = selectedCallBack

    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<FriendEntity>() {
            override fun areItemsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
                return oldItem.selected == newItem.selected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person_invite_room_selected, parent, false)
        return SelectedPersonViewHolder(ItemPersonInviteRoomSelectedBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectedPersonViewHolder).bind(getItem(position))
    }

    inner class SelectedPersonViewHolder(val binding: ItemPersonInviteRoomSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendEntity: FriendEntity) {
            if (friendEntity.preview.imageUrl != null) {
                Log.d(InviteRoomAdapter.TAG, "imageUrl - ${friendEntity.preview.imageUrl}")
                Glide.with(binding.ivInviteRoomSelectedImage).load(friendEntity.preview.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivInviteRoomSelectedImage)
            } else {
                Glide.with(binding.ivInviteRoomSelectedImage).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivInviteRoomSelectedImage)
            }

            binding.tvInviteRoomSelectedUserName.text = friendEntity.nickname
            binding.layoutInviteRoomSelected.setOnClickListener(ItemClickListener(friendEntity))
        }
    }

    inner class ItemClickListener(
        var friendEntity: FriendEntity
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            view?.disableClickTemporarily()
            when (view?.id) {
                R.id.layout_invite_room_selected -> {
                    itemClickCallBack?.onItemSelectedClick(friendEntity)
                }
            }
        }
    }
}
