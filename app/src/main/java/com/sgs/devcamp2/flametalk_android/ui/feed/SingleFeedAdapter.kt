package com.sgs.devcamp2.flametalk_android.ui.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.devcamp2.flametalk_android.databinding.ItemSingleFeedBinding
import com.sgs.devcamp2.flametalk_android.network.response.feed.Feed

/**
 * @author 박소연
 * @created 2022/02/03
 * @updated
 * @desc 1번째 탭 친구 리스트 adapter
 *       친구 프로필을 선택하면 상세프로필을 볼 수 있음
 */

class SingleFeedAdapter(
    private val context: Context,
    private val data: List<Feed>
) : RecyclerView.Adapter<SingleFeedAdapter.FeedHorizentalViewHolder>() {
    // var data = arrayListOf<Feed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHorizentalViewHolder {
        return FeedHorizentalViewHolder(
            ItemSingleFeedBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FeedHorizentalViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class FeedHorizentalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemSingleFeedBinding

        constructor(binding: ItemSingleFeedBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(data: Feed) {
            initFriendList(data)
        }

        private fun initFriendList(data: Feed) {
            Glide.with(itemView).load(data.imageUrl).into(binding.imgInnerSingleFeed)
        }
    }

    companion object {
        final const val TAG = "FeedHorizentalAdapter"
    }
}
