package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.FriendListRes

/**
 * @author 김현극
 * @created 2022/01/12
 */
class InviteRoomMarkAdapter constructor(
    markCallback: ItemMarkClickCallBack
) : ListAdapter<FriendListRes, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemMarkClickCallBack {
        fun onItemMarkClicked(tempFriend: FriendListRes, position: Int, adapter: InviteRoomMarkAdapter)
    }

    var itemClickCallBack: ItemMarkClickCallBack? = markCallback

    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<FriendListRes>() {
            override fun areItemsTheSame(oldItem: FriendListRes, newItem: FriendListRes): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FriendListRes, newItem: FriendListRes): Boolean {
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
        fun bind(tempFriend: FriendListRes, position: Int) {
            binding.ivInviteRoomRadioButton.isActivated = tempFriend.selected != "0"
            binding.layoutInviteRoomItem.setOnClickListener(
                ItemClickListener(tempFriend, position)
            )
            binding.tvInviteRoomUserName.text = tempFriend.nickname
        }
    }

    inner class ItemClickListener(
        var tempFriend: FriendListRes,
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
        var list: MutableList<FriendListRes> = currentList.toMutableList()
        var tempFriend: FriendListRes = currentList[position].copy()
        tempFriend.selected = "1"
        list[position] = tempFriend
        submitList(list)
    }
    fun removeActivate(position: Int) {
        var list: MutableList<FriendListRes> = currentList.toMutableList()
        var tempFriend: FriendListRes = currentList[position].copy()
        tempFriend.selected = "0"
        list[position] = tempFriend
        submitList(list)
    }
}
