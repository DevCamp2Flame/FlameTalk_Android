package com.sgs.devcamp2.flametalk_android.data.dummy

import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatProfilePreview
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatRoomPreview
import com.sgs.devcamp2.flametalk_android.data.model.openchat.OpenChatRoomPreview

/**
 * @author 박소연
 * @created 2022/01/23
 * @desc 오픈채팅방 UI 테스트용 더미데이터
 *       통신 연결 후 없애야 한다
 */

// 멀티프로필
fun getOpenChatRoom() = arrayListOf(
    OpenChatRoomPreview(
        "1번 채팅",
        "#플레임 #데브캠프 #짱짱",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        22,
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    ),
    OpenChatRoomPreview(
        "2번 채팅",
        "#플레임 #데브캠프 #짱짱",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        52,
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    ),
    OpenChatRoomPreview(
        "3번 채팅",
        "#플레임 #데브캠프 #짱짱",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        2,
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    ),
    OpenChatRoomPreview(
        "4번 채팅",
        "#플레임 #데브캠프 #짱짱",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        13,
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    )
)

// 나의 오픈채팅방 리스트
fun getMyOpenChatRoom() = arrayListOf(
    MyOpenChatRoomPreview(
        "11번 채팅방",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        2,
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg"
    ),
    MyOpenChatRoomPreview(
        "12번 채팅방",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        2,
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg"
    ),
    MyOpenChatRoomPreview(
        "13번 채팅방",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        2,
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg"
    )
)

fun getMyOpenProfile() = arrayListOf(
    MyOpenChatProfilePreview(
        "소연",
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg"
    ),
    MyOpenChatProfilePreview(
        "플레임 팀장",
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg"
    )
)
