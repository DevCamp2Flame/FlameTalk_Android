package com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class UpdateOpenProfileReq(
    val nickname: String,
    val imageUrl: String?,
    val description: String?
)
