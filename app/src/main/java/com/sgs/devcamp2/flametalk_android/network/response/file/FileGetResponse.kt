package com.sgs.devcamp2.flametalk_android.network.response.file

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.data.model.FileData

/**
 * @author 박소연
 * @created 2022/02/06
 * @desc 파일 조회 Response Body
 *
 * [Header]
 * statusCode	{statusCode}
 * Location	    api/profile/{profileId}	성공한 경우 생성된 프로필 id
 * [Body]
 * status 	    Integer	응답 코드
 * message	    String	응답 메세지
 * data	        Object	data 참고
 */

@Keep
data class FileGetResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: FileData
)
