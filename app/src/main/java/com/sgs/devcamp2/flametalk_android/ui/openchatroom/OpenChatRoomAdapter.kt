package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.model.openchat.OpenChatRoomPreview
import com.sgs.devcamp2.flametalk_android.databinding.ItemOpenChatRoomBinding
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback

/**
 * @author 박소연
 * @created 2022/01/23
 * @updated 2022/01/16
 * @desc 오픈채팅 메인화면의 추천 채팅 리스트 adapter
 *       오픈채팅방 리스트를 클릭하면 특정 채팅방에 참여할 수 있음
 */

class OpenChatRoomAdapter(
    private val context: Context
) : ListAdapter<OpenChatRoomPreview, OpenChatRoomAdapter.OpenChatRoomHolder>(SimpleDiffUtilCallback()) {
    var data = arrayListOf<OpenChatRoomPreview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatRoomHolder {
        return OpenChatRoomHolder(
            ItemOpenChatRoomBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OpenChatRoomHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class OpenChatRoomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemOpenChatRoomBinding

        constructor(binding: ItemOpenChatRoomBinding) : this(binding.root) {
            Log.d("ViewHolder", " create")
            this.binding = binding
        }

        fun bind(data: OpenChatRoomPreview) {
            // TODO: 오픈 채팅방으로 이동
            itemView.setOnClickListener {
            }

            initFriendList(data)
        }

        private fun initFriendList(data: OpenChatRoomPreview) {
            Glide.with(itemView).load(data.thumbnail).apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgOpenChatRoom)
            Glide.with(itemView).load(data.hostProfile).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgOpenChatRoomOwner)
            binding.tvOpenChatRoomTitle.text = data.title
            binding.tvOpenChatRoomDesc.text = data.desc
            binding.tvOpenChatRoomCount.text = data.count.toString() + "명"
        }
    }

    companion object {
        final const val TAG = "FriendAdapter"
    }
}
