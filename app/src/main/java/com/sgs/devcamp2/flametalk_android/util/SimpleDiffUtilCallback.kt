package com.sgs.devcamp2.flametalk_android.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc RecyclerView DiffUtil 확장함수
 *       반환하는 데이터 타입, 모델에 상관없이 쓸 수 있음
 */

class SimpleDiffUtilCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}
