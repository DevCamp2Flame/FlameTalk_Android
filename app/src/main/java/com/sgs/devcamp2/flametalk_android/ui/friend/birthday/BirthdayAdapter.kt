package com.sgs.devcamp2.flametalk_android.ui.friend.birthday

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemFriendPreviewBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.ProfilePreview
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone

/**
 * @author 박소연
 * @created 2022/01/14
 * @updated 2022/01/18
 * @desc 1번째 탭 생일인 친구 리스트 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class BirthdayAdapter(
    private val context: Context
) : RecyclerView.Adapter<BirthdayAdapter.BirthdayViewHolder>() {
    var data = arrayListOf<ProfilePreview>()

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
            Log.d("ViewHolder", " create")
            this.binding = binding
        }

        fun bind(data: ProfilePreview) {
            // 친구 프로필 상세보기로 이동
            itemView.setOnClickListener {
                val friendToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(2, data)
                it.findNavController().navigate(friendToFriendProfileDirections)
            }

            initBirthdayList(data)
        }

        private fun initBirthdayList(data: ProfilePreview) {
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
}
