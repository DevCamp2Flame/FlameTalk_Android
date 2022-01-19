package com.sgs.devcamp2.flametalk_android.network.request

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author 박소연
 * @created 2022/01/19
 * @desc 프로필 생성 RequestBody
 */

@Keep
@JsonClass(generateAdapter = true)
data class CreateProfileRequest(
    @Json(name = "imageId")
    val imageId: Long?,
    @Json(name = "backgroundId")
    val backgroundId: Long?,
    @Json(name = "sticker")
    val sticker: Sticker?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "isDefault")
    val isDefault: Boolean
)

@Keep
@JsonClass(generateAdapter = true)
data class Sticker(
    @Json(name = "stickerId")
    val stickerId: Int,
    @Json(name = "positionX")
    val positionX: Double,
    @Json(name = "positionY")
    val positionY: Double
)
