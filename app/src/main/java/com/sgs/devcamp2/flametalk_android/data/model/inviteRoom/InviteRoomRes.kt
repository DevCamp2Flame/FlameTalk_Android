package com.sgs.devcamp2.flametalk_android.data.model.inviteRoom

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/01/29
 * inviteRoom ui에서 api  response 객체
 */
@Parcelize
data class InviteRoomRes(
    @SerializedName("roomId")
    val roomId: String,
    @SerializedName("hostId")
    val hostId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("isOpen")
    val isOpen: Boolean
) : Parcelable
