package com.sgs.devcamp2.flametalk_android.data.model.openchat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *  MyOpenChatProfilePreview
 *
 *   nickname 닉네임
 *   profile 프로필 이미지
 */

@Parcelize
data class MyOpenChatProfilePreview(
    val nickname: String,
    val profile: String
) : Parcelable
