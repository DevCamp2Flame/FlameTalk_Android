package com.sgs.devcamp2.flametalk_android.network.response.feed

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/03
 * @desc 피드 리스트 조회 Request Body
 *
 * [Data]
 * data
 * Field	    Type	Description
 * profileId	Long	프로필 id
 * feeds	    List	feeds 참고
 * [Feeds]
 * Field	    Type	       Description
 * feedId	    Long	        피드 id
 * imageUrl	    String	        S3 URL
 * isBackground	Boolean 	    배경 사진 여부
 * isLock   	Boolean	        나만 보기 여부
 * createdDate	LocalDateTime	피드 생성한 날짜
 * updatedDate	LocalDateTime	피드 수정한 날짜
 */

@Keep
data class ProfileFeedResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    @Keep
    data class Data(
        @SerializedName("profileId")
        val profileId: Int,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("profileImage")
        val profileImage: String,
        @SerializedName("feeds")
        val feeds: List<Feed>
    )
}
