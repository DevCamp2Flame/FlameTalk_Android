package com.sgs.devcamp2.flametalk_android.network.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SignUpRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("password_confirm")
    val password_confirm: String? = null,
    @SerializedName("birth")
    val birth: String? = null,
)
