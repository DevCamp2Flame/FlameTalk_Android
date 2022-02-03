package com.sgs.devcamp2.flametalk_android.ui.friend.phonebook

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgs.devcamp2.flametalk_android.databinding.ItemPhoneBookBinding

/**
 * @author 박소연
 * @created 2022/02/01
 * @desc 주소록 전화번호 리스트 adapter
 */

class PhoneBookAdapter(
    private val context: Context
) : RecyclerView.Adapter<PhoneBookAdapter.PhoneBookViewHolder>() {
    var data = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneBookViewHolder {
        return PhoneBookViewHolder(
            ItemPhoneBookBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PhoneBookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class PhoneBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: ItemPhoneBookBinding

        constructor(binding: ItemPhoneBookBinding) : this(binding.root) {
            Log.d("ViewHolder", " create")
            this.binding = binding
        }

        fun bind(data: String) {
            initMultiProfileList(data)
        }

        private fun initMultiProfileList(data: String) {
            binding.tvPhoneBookTitle.text = data
        }
    }
}
