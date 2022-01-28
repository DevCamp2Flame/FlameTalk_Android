package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenchatroom

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
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatRoomPreview
import com.sgs.devcamp2.flametalk_android.databinding.ItemMyOpenChatRoomBinding
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback

/**
 * @author 박소연
 * @created 2022/01/23
 * @desc 오픈채팅 메인화면의 나의 오픈 채팅 리스트 adapter
 *       오픈채팅방 리스트를 클릭하면 특정 채팅방에 참여할 수 있음
 */

class MyOpenChatRoomAdapter(
    private val context: Context
) : ListAdapter<MyOpenChatRoomPreview, MyOpenChatRoomAdapter.MyOpenChatRoomHolder>(
    SimpleDiffUtilCallback()
) {
    var data = arrayListOf<MyOpenChatRoomPreview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOpenChatRoomHolder {
        return MyOpenChatRoomHolder(
            ItemMyOpenChatRoomBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyOpenChatRoomHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class MyOpenChatRoomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemMyOpenChatRoomBinding

        constructor(binding: ItemMyOpenChatRoomBinding) : this(binding.root) {
            Log.d("ViewHolder", " create")
            this.binding = binding
        }

        fun bind(data: MyOpenChatRoomPreview) {
            // TODO: 오픈 채팅방으로 이동
            itemView.setOnClickListener {
            }

            initFriendList(data)
        }

        private fun initFriendList(data: MyOpenChatRoomPreview) {
            Glide.with(itemView).load(data.userProfile).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgMyOpenChatRoomProfile)
            Glide.with(itemView).load(data.thumbnail).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgMyOpenChatRoomBg)
            binding.tvMyOpenChatRoomTitle.text = data.title
            if (data.count > 3) {
                binding.tvMyOpenChatRoomType.text = "그룹채팅"
            } else {
                binding.tvMyOpenChatRoomType.text = "1:1채팅"
            }
        }
    }

    companion object {
        final const val TAG = "MyOpenChatRoomAdapter"
    }
}
