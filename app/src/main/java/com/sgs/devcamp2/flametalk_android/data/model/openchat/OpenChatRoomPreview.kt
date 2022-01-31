package com.sgs.devcamp2.flametalk_android.data.model.openchat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *  OpenChatRoomPreview
 *
 *   title 채팅방 제목
 *   desc 채팅방 소개글
 *   hostProfile 방장 프로필 이미지
 *   count 참여인원
 *   thumbnail 채팅방 프로필 이미지
 */

@Parcelize
data class OpenChatRoomPreview(
    val title: String,
    val desc: String,
    val hostProfile: String,
    val count: Int,
    val thumbnail: String
) : Parcelable
