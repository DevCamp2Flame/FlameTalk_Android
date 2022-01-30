package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile

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
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatProfilePreview
import com.sgs.devcamp2.flametalk_android.databinding.ItemMyOpenChatProfileBinding
import com.sgs.devcamp2.flametalk_android.util.SimpleDiffUtilCallback

/**
 * @author 박소연
 * @created 2022/01/23
 * @updated 2022/01/16
 * @desc 오픈채팅 메인화면의 나의 오픈 프로필 리스트 adapter
 *       오픈프로필을 클릭하면 특정 채팅방에 참여할 수 있음
 */

class MyOpenChatProfileAdapter(
    private val context: Context
) : ListAdapter<MyOpenChatProfilePreview, MyOpenChatProfileAdapter.OpenChatProfileHolder>(SimpleDiffUtilCallback()) {
    var data = arrayListOf<MyOpenChatProfilePreview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatProfileHolder {
        return OpenChatProfileHolder(
            ItemMyOpenChatProfileBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OpenChatProfileHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class OpenChatProfileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemMyOpenChatProfileBinding

        constructor(binding: ItemMyOpenChatProfileBinding) : this(binding.root) {
            Log.d("ViewHolder", " create")
            this.binding = binding
        }

        fun bind(data: MyOpenChatProfilePreview) {
            // TODO: 오픈 채팅방으로 이동
            itemView.setOnClickListener {
            }

            initFriendList(data)
        }

        private fun initFriendList(data: MyOpenChatProfilePreview) {
            Glide.with(itemView).load(data.profile).apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                .into(binding.imgMyOpenChatProfile)
            binding.tvMyOpenChatProfileNickname.text = data.nickname
        }
    }

    companion object {
        final const val TAG = "MyOpenChatProfileAdapter"
    }
}
