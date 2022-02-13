package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.TempFriend

/**
 * @author 김현국
 * @created 2022/01/12
 */
class InviteRoomAdapter(callback: InviteRoomFragment) :
    ListAdapter<TempFriend, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemClickCallBack {
        fun onItemClicked(tempFriend: TempFriend, position: Int, adapter: InviteRoomAdapter)
    }

    val itemClickCallBack = callback
    /**
     * diffutil을 통해서 id를 우선적으로 비교하고, item의 변경점을 비교한다.
     */
    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<TempFriend>() {
            override fun areItemsTheSame(oldItem: TempFriend, newItem: TempFriend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TempFriend, newItem: TempFriend): Boolean {
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
        fun bind(tempFriend: TempFriend, position: Int) {

            binding.ivInviteRoomRadioButton.isActivated = tempFriend.selected != "0"
            binding.layoutInviteRoomItem.setOnClickListener(
                ItemClickListener(tempFriend, position, binding)
            )
            binding.tvInviteRoomUserName.text = tempFriend.nickname
        }
    }

    inner class ItemClickListener(
        var tempFriend: TempFriend,
        var position: Int,
        var binding: ItemPersonInviteRoomBinding

    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_item -> {
                    if (binding.ivInviteRoomRadioButton.isActivated) {
                        Log.d(TAG, "binding.ivInviteRoomRadioButton.isSelected - ${binding.ivInviteRoomRadioButton.isSelected} called")
                        binding.ivInviteRoomRadioButton.isActivated = false
                    } else {
                        binding.ivInviteRoomRadioButton.isActivated = true
                    }
                    itemClickCallBack?.onItemClicked(tempFriend, position, this@InviteRoomAdapter)
                }
            }
        }
    }
    /**
     * 유저가 선택을 했다면, selected의 값을 1로 바꿔준다.
     */
    fun putActivate(position: Int) {
        var list: MutableList<TempFriend> = currentList.toMutableList()
        var tempFriend: TempFriend = currentList[position].copy()
        tempFriend.selected = "1"
        list[position] = tempFriend
        submitList(list)
    }
    /**
     * 유저가 취소를 하기 위해 재선택을 했다면 , selected 0으로 바꿔준다.
     */
    fun removeActivate(position: Int) {
        var list: MutableList<TempFriend> = currentList.toMutableList()
        var tempFriend: TempFriend = currentList[position].copy()
        tempFriend.selected = "0"
        list[position] = tempFriend
        submitList(list)
    }
}
