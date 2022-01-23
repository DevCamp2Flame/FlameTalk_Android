package com.sgs.devcamp2.flametalk_android.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author boris
 * @created 2022/01/21
 */
@Keep
@Serializable
data class ChatMessageDto(
    @SerializedName("roomId")
    val roomId: String,
    @SerializedName("writer")
    val writer: String,
    @SerializedName("message")
    val message: String
)
