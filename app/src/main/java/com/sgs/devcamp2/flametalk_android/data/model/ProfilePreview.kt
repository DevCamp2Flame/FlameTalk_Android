package com.sgs.devcamp2.flametalk_android.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/06
 * @desc 프로필 미리보기 리스트 item
 *
 * [Profiles]
 * id	        Long	프로필 id
 * imageUrl 	String	프로필 사진 S3 url
 * description	String	프로필 상태 메세지
 * isDefault	boolean	기본 프로필 여부
 */

@Keep
data class ProfilePreview(
    @SerializedName("id")
    val id: Long,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("isDefault")
    val isDefault: Boolean
)
