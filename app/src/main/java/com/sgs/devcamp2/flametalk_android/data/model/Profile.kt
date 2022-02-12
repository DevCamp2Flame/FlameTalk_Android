package com.sgs.devcamp2.flametalk_android.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author 박소연
 * @created 2022/01/31
 * @created 2022/02/06
 * @desc 프로필 정보
 *
 * [Profile]
 * profileId	Long	프로필 id
 * nickname	String	유저 닉네임
 * imageUrl	String	프로필 사진 url
 * bgImageUrl	String	프로필 배경 사진 url
 * sticker	JSON Array	프로필에 사용된 스티커 리스트. sticker 참고
 * description	String	프로필 상태 메세지
 * isDefault	boolean	기본 프로필 여부
 * updatedDate	LocalDateTime	프로필 수정한 날짜
 */

@Keep
@Parcelize
data class Profile(
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("bgImageUrl")
    val bgImageUrl: String?,
    @SerializedName("sticker")
    val sticker: List<Sticker> = emptyList(),
    @SerializedName("description")
    val description: String?,
    @SerializedName("isDefault")
    val isDefault: Boolean,
    @SerializedName("updatedDate")
    val updatedDate: String
) : Parcelable
