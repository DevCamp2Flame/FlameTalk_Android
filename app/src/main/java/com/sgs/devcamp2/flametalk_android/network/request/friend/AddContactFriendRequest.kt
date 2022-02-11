package com.sgs.devcamp2.flametalk_android.network.request.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 연락처 친구 추가 Request
 *
 * [Body]
 * phoneNumbers	String List	연락처 번호 리스트
 */

@Keep
data class AddContactFriendRequest(
    @SerializedName("phoneNumbers")
    val phoneNumbers: List<String>
)
