package com.sgs.devcamp2.flametalk_android.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author 박소연
 * @created 2022/02/06
 * @updated 2022/02/06
 * @desc 프로필을 꾸미는 스티커의 정보
 *
 * [Sticker]
 * stickerId	Integer	스티커 id
 * positionX	Double	스티커 x 좌표
 * positionY	Double	스티커 y 좌표
 */

@Keep
@Parcelize
data class Sticker(
    @SerializedName("stickerId")
    val stickerId: Int,
    @SerializedName("positionX")
    val positionX: Double,
    @SerializedName("positionY")
    val positionY: Double
) : Parcelable
