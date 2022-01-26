package com.sgs.devcamp2.flametalk_android.data.model.openchat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *  MyOpenChatRoomPreview
 *
 *   title 채팅방 제목
 *   userProfile 프로필 이미지
 *   count 참여 인원
 *   thumbnail 배경이미지
 */

@Parcelize
data class MyOpenChatRoomPreview(
    val title: String,
    val userProfile: String,
    val count: Int,
    val thumbnail: String
) : Parcelable
