package com.sgs.devcamp2.flametalk_android.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author 박소연
 * @created 2022/02/06
 * @desc 파일 정보
 *
 * [FileData]
 * fileId	    Long	        파일 id 값
 * title	    String	        파일 이름
 * extension	String	        파일 확장자
 * url	        String	        S3 URL
 * createdDate	LocalDateTime	파일 업로드한 날짜
 */

@Keep
@Parcelize
data class FileData(
    @SerializedName("fileId")
    val fileId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("extension")
    val extension: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("createdDate")
    val createdDate: String
) : Parcelable
