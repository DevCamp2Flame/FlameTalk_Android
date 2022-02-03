package com.sgs.devcamp2.flametalk_android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/01/31
 * 보여질 chat
 *
 */
@Parcelize
data class ChatEntity(
    val room_id: String,
    val sender_id: String,
    val contents: String,
) : Parcelable
