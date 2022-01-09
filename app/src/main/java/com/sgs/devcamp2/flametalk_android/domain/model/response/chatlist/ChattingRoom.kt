package com.sgs.devcamp2.flametalk_android.domain.model.response.chatlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.collections.ArrayList

@Parcelize
data class ChattingRoom(
    val id: Int,
    val title: String,
    val user_list: ArrayList<String>,
    // val created_at: Date,
    // val updated_at: Date
) : Parcelable
