package com.sgs.devcamp2.flametalk_android.ui.friend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.databinding.ItemFriendPreviewBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.friends.FriendAdapter
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone

/**
 * @author 박소연
 * @created 2022/01/14
 * @updated 2022/01/18
 * @desc 1번째 탭 생일인 친구 리스트 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class FriendBirthdayAdapter(
    private val context: Context
) : ListAdapter<Friend, FriendBirthdayAdapter.BirthdayViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<Friend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayViewHolder {
        return BirthdayViewHolder(
            ItemFriendPreviewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class BirthdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemFriendPreviewBinding

        constructor(binding: ItemFriendPreviewBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: Friend) {
            // 친구 프로필 상세보기로 이동
            itemView.setOnClickListener {
                val friendToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(
                        viewType = FriendAdapter.FRIEND_PROFILE,
                        profileId = data.preview.profileId,
                        friendId = data.friendId,
                        friendUserId = data.userId,
                        assignedProfileId = data.assignedProfileId
                    )
                it.findNavController().navigate(friendToFriendProfileDirections)
            }

            initBirthdayList(data)
        }

        private fun initBirthdayList(data: Friend) {
            Glide.with(itemView).load(data.preview.imageUrl)
                .transform(CenterCrop(), RoundedCorners(35))
                .into(binding.imgFriendPreview)
            binding.tvFriendPreviewNickname.text = data.nickname

            if (data.preview.description != null) {
                binding.tvFriendPreviewDesc.toVisible()
                binding.tvFriendPreviewDesc.text = data.preview.description
            } else {
                binding.tvFriendPreviewDesc.toVisibleGone()
            }
        }
    }

    companion object {
        const val FRIEND_PROFILE = 2
    }
}
