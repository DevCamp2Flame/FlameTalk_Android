package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.FriendEntity

/**
 * @author 김현극
 * @created 2022/01/12
 */
class InviteRoomMarkAdapter constructor(
    markCallback: ItemMarkClickCallBack
) : ListAdapter<FriendEntity, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemMarkClickCallBack {
        fun onItemMarkClicked(tempFriend: FriendEntity, position: Int, adapter: InviteRoomMarkAdapter)
    }

    var itemClickCallBack: ItemMarkClickCallBack? = markCallback

    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<FriendEntity>() {
            override fun areItemsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FriendEntity, newItem: FriendEntity): Boolean {
                return oldItem.selected == newItem.selected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person_invite_room, parent, false)
        return PersonViewHolder(ItemPersonInviteRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PersonViewHolder).bind(getItem(position), position)
    }

    inner class PersonViewHolder(val binding: ItemPersonInviteRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tempFriend: FriendEntity, position: Int) {
            binding.ivInviteRoomRadioButton.isActivated = tempFriend.selected != "0"
            binding.layoutInviteRoomItem.setOnClickListener(
                ItemClickListener(tempFriend, position)
            )
            binding.tvInviteRoomUserName.text = tempFriend.nickname
        }
    }

    inner class ItemClickListener(
        var tempFriend: FriendEntity,
        var position: Int
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_item -> {
                    itemClickCallBack?.onItemMarkClicked(tempFriend, position, this@InviteRoomMarkAdapter)
                }
            }
        }
    }
    fun putActivate(position: Int) {
        var list: MutableList<FriendEntity> = currentList.toMutableList()
        var tempFriend: FriendEntity = currentList[position].copy()
        tempFriend.selected = "1"
        list[position] = tempFriend
        submitList(list)
    }
    fun removeActivate(position: Int) {
        var list: MutableList<FriendEntity> = currentList.toMutableList()
        var tempFriend: FriendEntity = currentList[position].copy()
        tempFriend.selected = "0"
        list[position] = tempFriend
        submitList(list)
    }
}
