package com.sgs.devcamp2.flametalk_android.network.response.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    val id: Int?,
    val chatroom_id: String?,
    val user_id: String?,
    val content: String?,
    // val created_at: Date,
    // val updated_at: Date
) : Parcelable
