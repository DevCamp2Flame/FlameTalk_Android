package com.sgs.devcamp2.flametalk_android.network.response.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview

/**
 * @author 박소연
 * @created 2022/02/06
 * @desc 프로필 리스트 Response Body
 *
 * [Body]
 * code     	Integer	응답 코드
 * message	    String	응답 메세지
 * data	        Object	data 참고
 * [Data]
 * userId	    String	유저 id
 * nickname	    String	유저 닉네임
 * profiles	    List	profiles 참고
 * [Profiles]
 * id	        Long	프로필 id
 * imageUrl 	String	프로필 사진 S3 url
 * description	String	프로필 상태 메세지
 * isDefault	boolean	기본 프로필 여부
 */

@Keep
data class ProfileListResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    @Keep
    data class Data(
        @SerializedName("userId")
        val userId: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("profiles")
        val profiles: List<ProfilePreview>
    )
}
