package com.sgs.devcamp2.flametalk_android.domain.model.response.chatlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.collections.ArrayList

@Parcelize
data class ChatList(
    val id: Int?,
    val chatroom_id: String?,
    val user_id: String?,
    val title: String?,
    val user_list: ArrayList<String>,
    // val created_at: Date,
    // val updated_at: Date
) : Parcelable
