package com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/02/05
 */
data class GetOpenProfileListRes(
    val userId: String,
    val openProfiles: List<OpenProfile>
)
@Parcelize
data class OpenProfile(
    val openProfileId: Long,
    val nickname: String,
    val imageUrl: String,
    val description: String,
    val createdDate: String,
    val updatedDate: String
) : Parcelable
