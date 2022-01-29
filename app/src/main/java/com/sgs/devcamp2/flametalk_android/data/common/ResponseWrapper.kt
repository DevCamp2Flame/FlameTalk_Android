package com.sgs.devcamp2.flametalk_android.data.common

import com.google.gson.annotations.SerializedName

/**
 * @author boris
 * @created 2022/01/28
 * 서버 response를 파싱할 때 사용하는 wrapper
 */
data class WrappedListResponse<T> (
    var code: Int,
    @SerializedName("message") var message: String,
    @SerializedName("status") var status: Boolean,
    @SerializedName("errors") var errors: List<String>? = null,
    @SerializedName("data") var data: List<T>? = null
)

data class WrappedResponse<T> (
    @SerializedName("status") var status: Int,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: T? = null,
    @SerializedName("errors") var errors: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("timestamp") var timestamp: String? = null
)
