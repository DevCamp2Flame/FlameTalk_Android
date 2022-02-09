package com.sgs.devcamp2.flametalk_android.network.response.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 친구 상태 변경(숨김, 차단) Response
 *
 * [Body]
 * code	    Integer	응답 코드
 * message	String	응답 메세지
 * data	    Object	응답 메세지. data 참고
 *
 * [Data]
 * friendId	            String	친구 관계 id
 * userId	            String	친구의 유저 id
 * nickname         	String	친구의 유저 닉네임
 * assignedProfileId	Long	친구에게 보여줄 나의 프로필 id
 * type	                String	친구 관계 타입. DEFAULT, MARKED, HIDDEN, BLOCKED 중 하나
 * preview	            Object	친구 유저 프로필. preview 참고
 */

@Keep
data class FriendStatusResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    @Keep
    data class Data(
        @SerializedName("friendId")
        val friendId: String,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("assignedProfileId")
        val assignedProfileId: Long,
        @SerializedName("type")
        val type: String,
        @SerializedName("preview")
        val preview: SimpleProfile,
    )
}

/**
 * [SimpleProfile]
 * profileId	Long	친구 유저 프로필 id
 * imageUrl	    String	친구 유저 프로필 사진 S3 URL
 * description	String	친구 유저 프로필 상태 메세지
 */

@Keep
data class SimpleProfile(
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("description")
    val description: String
)
