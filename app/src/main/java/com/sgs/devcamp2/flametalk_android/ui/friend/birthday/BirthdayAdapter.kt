package com.sgs.devcamp2.flametalk_android.ui.friend.birthday

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemFriendPreviewBinding
import com.sgs.devcamp2.flametalk_android.network.response.friend.ProfilePreview

/**
 * @author 박소연
 * @created 2022/01/14
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

            binding.root.setOnClickListener {
                // TODO: userID 넘겨서 프로필 상세 화면으로 이동 (id)
            }
        }

        fun bind(data: ProfilePreview) {
            Glide.with(itemView).load(data.image).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgFriendPreview)
            binding.tvFriendPreviewNickname.text = data.nickname
            if (data.description != null) {
                binding.tvFriendPreviewDesc.visibility = View.VISIBLE
                binding.tvFriendPreviewDesc.text = data.description
            } else {
                binding.tvFriendPreviewDesc.visibility = View.GONE
            }
        }
    }
}
