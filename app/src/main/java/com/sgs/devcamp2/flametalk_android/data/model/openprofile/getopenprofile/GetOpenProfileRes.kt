package com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofile

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class GetOpenProfileRes(
    val openProfileId: Long,
    val userId: String,
    val nickname: String,
    val imageUrl: String,
    val description: String,
    val createdDate: String,
    val updatedDate: String
)
