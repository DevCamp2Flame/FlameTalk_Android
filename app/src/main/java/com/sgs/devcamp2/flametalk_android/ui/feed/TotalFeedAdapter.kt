package com.sgs.devcamp2.flametalk_android.ui.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ItemTotalFeedBinding
import com.sgs.devcamp2.flametalk_android.network.response.feed.Feed
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone

/**
 * @author 박소연
 * @created 2022/02/02
 * @created 2022/02/16
 * @desc 프로필과 배경화면의 변경 이력을 보여주는 수직 스크롤 형태의 피드 리스트 adapter
 */

class TotalFeedAdapter(
    private val context: Context,
    private val profileImage: String?,
    val onClickDeleteItem: (feed: Feed) -> Unit,
    val onClickChangeLockItem: (feed: Feed) -> Unit
) : ListAdapter<Feed, TotalFeedAdapter.ViewHolder>(SimpleDiffUtilCallback()) {
    var data = listOf<Feed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTotalFeedBinding.inflate(
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
        lateinit var binding: ItemTotalFeedBinding

        constructor(binding: ItemTotalFeedBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: Feed) {
            initMultiProfileList(data)
            initPopupMenu(data)
        }

        private fun initMultiProfileList(data: Feed) {
            if (data.isBackground) {
                binding.tvVerticalProfileType.text = "나의 배경"
            } else {
                binding.tvVerticalProfileType.text = "나의 프로필"
            }
            // 유저 프로필 이미지
            Glide.with(itemView).load(profileImage)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgTotalFeedThumbnail)
            binding.tvTotalFeedUpdate.text = data.updatedDate
            Glide.with(itemView).load(data.imageUrl).into(binding.imgTotalFeed)
            if (data.isLock) {
                binding.tvTotalFeedPrivate.toVisible()
            } else {
                binding.tvTotalFeedPrivate.toVisibleGone()
            }
        }

        private fun initPopupMenu(data: Feed) {
            // 옵션 메뉴: 다운로드, 피드 삭제, 사진 공개 범위 변경
            binding.imgTotalFeedMore.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, binding.imgTotalFeedMore)
                popupMenu.inflate(R.menu.feed_menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_lock -> {
                            // TODO: RecyclerView에서 viewmodel의 함수 호출해야 함
                            onClickChangeLockItem.invoke(data)
                        }
                        R.id.menu_delete -> {
                            // TODO: RecyclerView에서 viewmodel의 함수 호출해야 함
                            onClickDeleteItem.invoke(data)
                        }
                    }
                    return@setOnMenuItemClickListener false
                }
                popupMenu.show()
            }
        }
    }
}
