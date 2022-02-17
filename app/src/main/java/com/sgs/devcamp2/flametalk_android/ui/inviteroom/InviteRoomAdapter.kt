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
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom.FriendEntity

/**
 * @author 김현국
 * @created 2022/01/12
 */
class InviteRoomAdapter(callback: InviteRoomFragment) :
    ListAdapter<FriendEntity, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemClickCallBack {
        fun onItemClicked(tempFriend: FriendEntity, position: Int, adapter: InviteRoomAdapter)
    }

    val itemClickCallBack = callback
    /**
     * diffutil을 통해서 id를 우선적으로 비교하고, item의 변경점을 비교한다.
     */
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
            .inflate(R.layout.item_person_invite_room, parent, false)
        return PersonViewHolder(ItemPersonInviteRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PersonViewHolder).bind(getItem(position), position)
    }

    inner class PersonViewHolder(val binding: ItemPersonInviteRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendEntity: FriendEntity, position: Int) {

            if (friendEntity.preview.imageUrl != null) {
                Log.d(TAG, "imageUrl - ${friendEntity.preview.imageUrl}")
                Glide.with(binding.ivInviteRoomImage).load(friendEntity.preview.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivInviteRoomImage)
            } else {
                Glide.with(binding.ivInviteRoomImage).load(R.drawable.ic_person_white_24)
                    .transform(CenterCrop(), RoundedCorners(40)).into(binding.ivInviteRoomImage)
            }

            binding.ivInviteRoomRadioButton.isActivated = friendEntity.selected != "0"
            binding.layoutInviteRoomItem.setOnClickListener(
                ItemClickListener(friendEntity, position, binding)
            )
            binding.tvInviteRoomUserName.text = friendEntity.nickname
        }
    }

    inner class ItemClickListener(
        var friendEntity: FriendEntity,
        var position: Int,
        var binding: ItemPersonInviteRoomBinding

    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_item -> {
                    binding.ivInviteRoomRadioButton.isActivated = !binding.ivInviteRoomRadioButton.isActivated
                    itemClickCallBack?.onItemClicked(friendEntity, position, this@InviteRoomAdapter)
                }
            }
        }
    }
    /**
     * 유저가 선택을 했다면, selected의 값을 1로 바꿔준다.
     */
    fun putActivate(position: Int) {
        var list: MutableList<FriendEntity> = currentList.toMutableList()
        var friendEntity: FriendEntity = currentList[position].copy()
        friendEntity.selected = "1"
        list[position] = friendEntity
        submitList(list)
    }
    /**
     * 유저가 취소를 하기 위해 재선택을 했다면 , selected 0으로 바꿔준다.
     */
    fun removeActivate(position: Int) {
        var list: MutableList<FriendEntity> = currentList.toMutableList()
        var friendEntity: FriendEntity = currentList[position].copy()
        friendEntity.selected = "0"
        list[position] = friendEntity
        submitList(list)
    }
}
