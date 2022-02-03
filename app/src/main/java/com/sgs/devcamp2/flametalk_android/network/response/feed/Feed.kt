package com.sgs.devcamp2.flametalk_android.network.response.feed

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Feed(
    @SerializedName("feedId")
    val feedId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("isBackground")
    val isBackground: Boolean,
    @SerializedName("isLock")
    val isLock: Boolean,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("updatedDate")
    val updatedDate: String
)
