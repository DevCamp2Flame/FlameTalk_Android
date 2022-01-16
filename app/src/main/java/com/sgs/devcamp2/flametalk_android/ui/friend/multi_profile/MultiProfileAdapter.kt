package com.sgs.devcamp2.flametalk_android.ui.friend.multi_profile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.databinding.ItemVerticalProfileBinding
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.ProfilePreview

/**
 * @author 박소연
 * @created 2022/01/14
 * @desc 1번째 탭의 메인 유저의 멀티프로필 리스트 adapter
 *       유저가 멀티프로필을 선택하고 추가할 수 있음
 */

class MultiProfileAdapter(
    private val context: Context
) : RecyclerView.Adapter<MultiProfileAdapter.MultiProfileViewHolder>() {
    var data = arrayListOf<ProfilePreview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiProfileViewHolder {
        return MultiProfileViewHolder(
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

    override fun onBindViewHolder(holder: MultiProfileViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class MultiProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemVerticalProfileBinding

        constructor(binding: ItemVerticalProfileBinding) : this(binding.root) {
            Log.d("ViewHolder", " create")
            this.binding = binding

            binding.root.setOnClickListener {
                // TODO: userID 넘겨서 프로필 상세 화면으로 이동 (id)
            }
        }

        fun bind(data: ProfilePreview) {
            Glide.with(itemView).load(data.image).apply(RequestOptions.circleCropTransform())
                .into(binding.imgVerticalProfile)
            binding.tvVerticalProfileNickname.text = data.nickname
        }
    }
}
