package com.sgs.devcamp2.flametalk_android.ui.friend.birthday

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
import com.sgs.devcamp2.flametalk_android.databinding.ItemBirthdayPreviewBinding
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone

/**
 * @author 박소연
 * @created 2022/02/18
 * @updated 2022/02/18
 * @desc 생일 친구 리스트 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class BirthdayAdapter(
    private val context: Context
) : ListAdapter<Friend, BirthdayAdapter.ViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<Friend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBirthdayPreviewBinding.inflate(
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
        lateinit var binding: ItemBirthdayPreviewBinding

        constructor(binding: ItemBirthdayPreviewBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: Friend) {
            // 이미지를 누르면 친구 프로필로 이동
            binding.root.setOnClickListener {
                val birthdayToFriendProfileDirections: NavDirections =
                    BirthdayFragmentDirections.actionBirthdayToProfile(
                        FRIEND_PROFILE,
                        data.preview.profileId
                    )
                it.findNavController().navigate(birthdayToFriendProfileDirections)
            }

            initFriendList(data)
        }

        private fun initFriendList(data: Friend) {
            Glide.with(itemView).load(data.preview.imageUrl)
                .transform(CenterCrop(), RoundedCorners(35))
                .into(binding.imgBirthdayPreview)
            binding.tvBirthdayPreviewNickname.text = data.nickname
            if (data.preview.description == null) {
                binding.tvBirthdayPreviewDesc.toVisibleGone()
            } else {
                binding.tvBirthdayPreviewDesc.text = data.preview.description
            }
        }
    }

    companion object {
        const val FRIEND_PROFILE = 2
    }
}
