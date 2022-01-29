package com.sgs.devcamp2.flametalk_android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/01/28
 * Room,Retrofit 등과 연관되지 않는 순수 DATA CLASS 이며,
 * 실제 필요한 데이터만 가지고 있다.
 */
@Parcelize
data class ChatRoomsEntity(
    val room_id: String,
    val host_id: String,
    val title: String,
    val is_open: Boolean,
    val user_size: Int
) : Parcelable
