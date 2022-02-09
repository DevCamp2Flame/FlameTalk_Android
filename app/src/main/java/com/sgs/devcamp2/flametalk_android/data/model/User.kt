package com.sgs.devcamp2.flametalk_android.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userId: String,
    val nickname: String,
    val status: String,
    val accessToken: String,
    val refreshToken: String
) : Parcelable
