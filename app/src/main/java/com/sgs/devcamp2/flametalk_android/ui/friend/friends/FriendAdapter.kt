package com.sgs.devcamp2.flametalk_android.ui.friend.friends

import android.content.Context
import android.util.Log
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
import com.sgs.devcamp2.flametalk_android.databinding.ItemFriendPreviewBinding
import com.sgs.devcamp2.flametalk_android.data.model.ProfilePreview
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone

/**
 * @author 박소연
 * @created 2022/01/14
 * @updated 2022/01/18
 * @desc 1번째 탭 친구 리스트 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class FriendAdapter(
    private val context: Context
) : ListAdapter<ProfilePreview, FriendAdapter.FriendViewHolder>(SimpleDiffUtilCallback()) {
    var data = arrayListOf<ProfilePreview>()

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
            Log.d("ViewHolder", " create")
            this.binding = binding
        }

        fun bind(data: ProfilePreview) {
            // 친구 프로필 상세보기로 이동
            itemView.setOnClickListener {
                val friendToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(2, data, data.userId.toString())
                it.findNavController().navigate(friendToFriendProfileDirections)
            }

            initFriendList(data)
        }

        private fun initFriendList(data: ProfilePreview) {
            Glide.with(itemView).load(data.image).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgFriendPreview)
            binding.tvFriendPreviewNickname.text = data.nickname

            if (data.description != null) {
                binding.tvFriendPreviewDesc.toVisible()
                binding.tvFriendPreviewDesc.text = data.description
            } else {
                binding.tvFriendPreviewDesc.toVisibleGone()
            }
        }
    }

    companion object {
        final const val TAG = "FriendAdapter"
    }
}
