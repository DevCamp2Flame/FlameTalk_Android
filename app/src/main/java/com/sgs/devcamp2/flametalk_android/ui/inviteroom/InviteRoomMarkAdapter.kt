package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.Friend

/**
 * @author boris
 * @created 2022/01/12
 */
class InviteRoomMarkAdapter constructor(
    markCallback: ItemClickCallBack
) : ListAdapter<Friend, RecyclerView.ViewHolder>(diffUtil) {
    interface ItemClickCallBack {
        fun onItemClicked(friend: Friend)
    }

    var itemClickCallBack: ItemClickCallBack? = markCallback

    companion object {
        val TAG: String = "로그"
        val diffUtil = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person_invite_room, parent, false)
        return PersonViewHolder(ItemPersonInviteRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PersonViewHolder).bind(getItem(position))
    }

    inner class PersonViewHolder(val binding: ItemPersonInviteRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.tvInviteRoomUserName.text = friend.nickname
            binding.layoutInviteRoomItem.setOnClickListener(
                ItemClickListener(
                    friend, binding
                )
            )
        }
    }
    /**
     * change visiblity from radioButton
     * if item touched filledRadiobutton set visible and radiobutton set visible gone
     * else radiobutton set visible and filledRadiobutton set visible gone
     *
     */
    inner class ItemClickListener(
        var friend: Friend,
        var binding: ItemPersonInviteRoomBinding
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_item -> {
//                    if (friend.selected) {
//                        this.binding.ivInviteRoomRadioButtonFill.visibility = View.GONE
//                        this.binding.ivInviteRoomRadioButton.visibility = View.VISIBLE
//                    } else {
//                        this.binding.ivInviteRoomRadioButton.visibility = View.GONE
//                        this.binding.ivInviteRoomRadioButtonFill.visibility = View.VISIBLE
//                    }
//                    if (this.binding.ivInviteRoomRadioButton.visibility == View.VISIBLE) {
//                        this.binding.ivInviteRoomRadioButton.visibility = View.GONE
//                        this.binding.ivInviteRoomRadioButtonFill.visibility = View.VISIBLE
//                    } else {
//                        this.binding.ivInviteRoomRadioButtonFill.visibility = View.GONE
//                        this.binding.ivInviteRoomRadioButton.visibility = View.VISIBLE
//                    }
                    itemClickCallBack?.onItemClicked(friend)
                }
            }
        }
    }
}
