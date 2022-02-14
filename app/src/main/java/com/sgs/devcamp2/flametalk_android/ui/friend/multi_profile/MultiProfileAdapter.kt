package com.sgs.devcamp2.flametalk_android.ui.friend.multi_profile

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
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview
import com.sgs.devcamp2.flametalk_android.databinding.ItemVerticalProfileBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/01/14
 * @updated 2022/02/06
 * @desc 1번째 탭의 메인 유저의 멀티프로필 리스트 adapter
 *       유저가 멀티프로필을 선택하고 추가할 수 있음
 */

class MultiProfileAdapter(
    private val context: Context
) : ListAdapter<ProfilePreview, MultiProfileAdapter.ViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<ProfilePreview>()
    var nickname: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVerticalProfileBinding.inflate(
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
        lateinit var binding: ItemVerticalProfileBinding

        constructor(binding: ItemVerticalProfileBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: ProfilePreview) {
            initMultiProfileList(data)

            // 멀티 프로필 상세보기로 이동
            itemView.setOnClickListener {
                val friendToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(USER_MULTI_PROFILE, data.id)
                it.findNavController().navigate(friendToFriendProfileDirections)
            }
        }

        private fun initMultiProfileList(data: ProfilePreview) {
            Glide.with(itemView).load(data.imageUrl).transform(CenterCrop(), RoundedCorners(35))
                .into(binding.imgVerticalProfile)
            binding.tvVerticalProfileNickname.text = nickname
            Timber.d("nickname? $nickname")
        }
    }

    companion object {
        const val USER_MULTI_PROFILE = 3
    }
}
