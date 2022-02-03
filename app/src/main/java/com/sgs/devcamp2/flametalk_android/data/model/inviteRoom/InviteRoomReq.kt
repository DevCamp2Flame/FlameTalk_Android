package com.sgs.devcamp2.flametalk_android.data.model.inviteroom

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/01/29
 * inviteRoom ui에서 api  request 객체
 */
@Parcelize
data class InviteRoomReq(
    @SerializedName("hostId")
    val hostId: String,
    @SerializedName("isOpen")
    val isOpen: Boolean,
    @SerializedName("users")
    val users: List<String>

) : Parcelable
