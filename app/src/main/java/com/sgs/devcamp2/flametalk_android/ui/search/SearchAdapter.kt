package com.sgs.devcamp2.flametalk_android.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendModel
import com.sgs.devcamp2.flametalk_android.databinding.ItemFriendPreviewBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone

/**
 * @author 박소연
 * @created 2022/02/11
 * @updated 2022/02/13
 * @desc 친구 검색 결과 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class SearchAdapter(
    private val context: Context
) : ListAdapter<FriendModel, SearchAdapter.FriendViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<FriendModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
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

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemFriendPreviewBinding

        constructor(binding: ItemFriendPreviewBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: FriendModel) {
            // 친구 프로필 상세보기로 이동
            itemView.setOnClickListener {
                val friendToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(
                        FRIEND_PROFILE,
                        data.profileId
                    )
                it.findNavController().navigate(friendToFriendProfileDirections)
            }

            initFriendList(data)
        }

        private fun initFriendList(data: FriendModel) {
            Glide.with(itemView).load(data.imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgFriendPreview)
            binding.tvFriendPreviewNickname.text = data.nickname

            if (data.description!!.isNotEmpty()) {
                binding.tvFriendPreviewDesc.toVisible()
                binding.tvFriendPreviewDesc.text = data.description
            } else {
                binding.tvFriendPreviewDesc.toVisibleGone()
            }
        }
    }

    companion object {
        const val FRIEND_PROFILE = 2
    }
}
