package com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class CreateOpenProfileReq(
    val userId: String,
    val nickname: String,
    val imageUrl: String?,
    val description: String?
)
