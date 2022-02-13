package com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class UpdateOpenProfileRes(
    val openProfileId: Long,
    val userId: String,
    val nickname: String,
    val imageUrl: String,
    val description: String,
    val creaetedDate: String,
    val updatedDate: String
)
