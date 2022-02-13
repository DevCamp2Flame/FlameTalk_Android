package com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom

/**
 * @author 김현국
 * @created 2022/02/05
 * 채팅방에 입장하여 오른쪽 drawer를 눌렀을 때의 상세 조회
 */
data class GetChatRoomRes(
    val isOpen: Boolean,
    val profileId: Long,
    val profileNickname: String,
    val profileImage: String,
    val profiles: List<Profile>,
    val files: List<String>
)

data class Profile(
    val id: Long,
    val nickname: String,
    val image: String
)
