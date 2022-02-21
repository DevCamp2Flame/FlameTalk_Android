package com.sgs.devcamp2.flametalk_android.ui.friend.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview
import com.sgs.devcamp2.flametalk_android.databinding.ItemVerticalProfileBinding
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback

/**
 * @author 박소연
 * @created 2022/02/11
 * @updated 2022/02/11
 * @desc 친구 추가 시 보여줄 프로필을 선택해야 한다.
 *       초기에는 선택이 안되어있음. 한번 선택을 하면 해제할 수 없음. single 선택임
 */

class SelectProfileAdapter(
    private val context: Context,
    private val nickname: String,
    private val onClicked: (ProfilePreview) -> Unit
) : ListAdapter<ProfilePreview, SelectProfileAdapter.ViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<ProfilePreview>()
    var clickedItem = 0L
    var clickedView: View? = null

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

    inner class ViewHolder(
        private var binding: ItemVerticalProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProfilePreview) {
            binding.imgVerticalProfile.setBackgroundResource(R.color.flame_sky_blue)
            Glide.with(itemView).load(data.imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgVerticalProfile)
            binding.tvVerticalProfileNickname.text = nickname

            if (data.id != clickedItem) { // 클릭되지 않았고 선택되지 않은 아이템
                binding.vVerticalProfile.isSelected = false
            }

            binding.vVerticalProfile.setOnClickListener {
                onClicked.invoke(data)
                clickedItem = data.id
                clickedView?.isSelected = false

                if (clickedView != it) { // 재클릭
                    clickedView = it
                    it.isSelected = true
                }
            }
        }
    }
}
