package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemMarkPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomBinding
import com.sgs.devcamp2.flametalk_android.databinding.ItemPersonInviteRoomSelectedBinding
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.Friend

/**
 * @author boris
 * @created 2022/01/14
 */
class Adapter constructor(
    callback: ClickCallBack
) : ListAdapter<Friend, RecyclerView.ViewHolder>(diffUtil) {

    interface ClickCallBack {
        fun onItemClicked(friend: Friend)
    }

    var itemClicked: ClickCallBack? = callback
    companion object {
        val TAG: String = "로그"

        private const val NOTMARKED = 0
        private const val MARKED = 1

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

        val view: View?
        return when (viewType) {
            NOTMARKED ->
                {
                    // not marked

                    view = LayoutInflater.from(parent.context).inflate(R.layout.item_person_invite_room, parent, false)
                    PersonViewHolder(ItemPersonInviteRoomBinding.bind(view))
                }
            else ->
                {
                    // marked
                    view = LayoutInflater.from(parent.context).inflate(R.layout.item_mark_person_invite_room, parent, false)
                    MarkViewHolder(ItemMarkPersonInviteRoomBinding.bind(view))
                }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItem(position).mark) {
            NOTMARKED ->
                {

                    (holder as PersonViewHolder).bind(getItem(position), position)
                }
            MARKED ->
                {

                    (holder as MarkViewHolder).bind(getItem(position), position)
                }
        }
    }
    /**
     * currentList 자체는 변경을 할 수 없는 리스트다
     */

    override fun getItemViewType(position: Int): Int {
        return currentList[position].mark
    }

    inner class PersonViewHolder(val binding: ItemPersonInviteRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend, position: Int) {

            binding.layoutInviteRoomItem.setOnClickListener(ItemClickListener(friend, binding, null, null, position))
            binding.tvInviteRoomUserName.text = friend.nickname
        }
    }

    inner class MarkViewHolder(val binding: ItemMarkPersonInviteRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend, position: Int) {

            binding.layoutMarkInviteRoomItem.setOnClickListener(ItemClickListener(friend, null, binding, null, position))
            binding.tvMarkInviteRoomUserName.text = friend.nickname
        }
    }

    inner class SelectedPersonViewHolder(val binding: ItemPersonInviteRoomSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.layoutInviteRoomSelected.setOnClickListener(ItemClickListener(friend, null, null, binding, position = null))
            binding.tvInviteRoomSelectedUserName.text = friend.nickname
        }
    }

    inner class ItemClickListener(
        var friend: Friend,
        var inviteBinding: ItemPersonInviteRoomBinding?,
        var markBindng: ItemMarkPersonInviteRoomBinding?,
        var selectedBinding: ItemPersonInviteRoomSelectedBinding?,
        var position: Int?
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                Log.d(TAG, "ItemClickListener - view : $view called"),
                R.id.layout_invite_room_item ->
                    {

                        val newList = currentList.toMutableList()
                        if (friend.selected == 0) {
                            newList[position!!].selected = 1
                            submitList(newList)
                            inviteBinding?.ivInviteRoomRadioButton?.visibility = View.GONE
                            inviteBinding?.ivInviteRoomRadioButtonFill?.visibility = View.VISIBLE
                        } else {
                            newList[position!!].selected = 0
                            submitList(newList)
                            inviteBinding?.ivInviteRoomRadioButton?.visibility = View.VISIBLE
                            inviteBinding?.ivInviteRoomRadioButtonFill?.visibility = View.GONE
                        }

//
                    }
                R.id.layout_mark_invite_room_item ->
                    {
                        val newList = currentList.toMutableList()
                        if (friend.selected == 0) {
                            newList[position!!].selected = 1
                            submitList(newList)
//                            markBindng?.ivMarkInviteRoomRadioButton?.visibility = View.GONE
//                            markBindng?.ivMarkInviteRoomRadioButtonFill?.visibility = View.VISIBLE
                        } else {
                            newList[position!!].selected = 0
                            submitList(newList)
//                            markBindng?.ivMarkInviteRoomRadioButton?.visibility = View.VISIBLE
//                            markBindng?.ivMarkInviteRoomRadioButtonFill?.visibility = View.GONE
                        }
                    }
                R.id.layout_invite_room_selected ->
                    {
                        itemClicked?.onItemClicked(friend)
                    }
            }
        }
    }
}
