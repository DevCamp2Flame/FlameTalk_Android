package com.sgs.devcamp2.flametalk_android.domain.model.response.friend

/**
 *  ProfilePreview
 *
 *   nickname 유저 닉네임
 *   image 프로필 이미지
 *   description 상태 메세지
 *   backgroundImage 배경 이미지
 */

data class ProfilePreview(
    val userId: Int,
    val nickname: String,
    val image: String?,
    val description: String?,
    val backgroundImage: String?
)
