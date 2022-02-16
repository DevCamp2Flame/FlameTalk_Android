package com.sgs.devcamp2.flametalk_android.ui.friend.blocked

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
import com.sgs.devcamp2.flametalk_android.databinding.ItemFriendBlockBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 숨김 친구 리스트 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class BlockedAdapter(
    private val context: Context,
    val onClickProfile: (friend: Friend) -> Unit,
    val onChangeBlock: (friend: Friend) -> Unit
) : ListAdapter<Friend, BlockedAdapter.ViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<Friend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFriendBlockBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemFriendBlockBinding

        constructor(binding: ItemFriendBlockBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: Friend) {
            // 이미지를 누르면 친구 프로필로 이동 // 프로필 타입이 몇개더라..
            binding.imgFriendBlock.setOnClickListener {
                val hiddenToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(
                        FRIEND_PROFILE,
                        data.preview.profileId
                    )
                it.findNavController().navigate(hiddenToFriendProfileDirections)
            }

            // 숨김 여부를 누르면 숨김 여부 변경 callback을 return
            binding.tvFriendBlock.setOnClickListener {
                onChangeBlock(data)
                it.isSelected = !it.isSelected
            }

            initFriendList(data)
        }

        private fun initFriendList(data: Friend) {
            Glide.with(itemView).load(data.preview.imageUrl)
                .transform(CenterCrop(), RoundedCorners(35))
                .into(binding.imgFriendBlock)
            binding.tvFriendBlockNickname.text = data.nickname
            binding.tvFriendBlock.isSelected = true
        }
    }

    companion object {
        const val FRIEND_PROFILE = 2
    }
}
