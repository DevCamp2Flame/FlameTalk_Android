package com.sgs.devcamp2.flametalk_android.network.response.friend

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *  ProfilePreview
 *
 *   nickname 유저 닉네임
 *   image 프로필 이미지
 *   description 상태 메세지
 *   backgroundImage 배경 이미지
 */

// TODO:친구 리스트 통신 후 삭제해야 한다.
@Parcelize
data class ProfilePreview(
    val userId: Int,
    val profileId: Long,
    val nickname: String,
    val image: String?,
    val description: String?,
    val backgroundImage: String?,
    val isDefault: Boolean = false
) : Parcelable
