package com.sgs.devcamp2.flametalk_android.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

/**
 * @author 박소연
 * @created 2022/01/19
 * @desc 프로필 생성 RequestBody
 */

@Keep
data class FileCreateRequest(
    @SerializedName("file")
    val file: MultipartBody.Part,
    @SerializedName("chatroomId")
    val chatroomId: String?
)
