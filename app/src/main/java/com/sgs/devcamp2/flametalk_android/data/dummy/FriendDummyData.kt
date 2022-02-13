package com.sgs.devcamp2.flametalk_android.data.dummy

import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendModel
import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendPreview
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfileDummyPreview
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview

/**
 * @author 박소연
 * @created 2022/01/14
 * @desc 1번째 탭 친구 리스트 UI 테스트용 더미데이터
 *       통신 연결 후 없애야 한다
 */

// 유저 정보 1개
fun getDummyUser() = ProfileDummyPreview(
    1,
    1,
    "박소연",
    "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    "아자아자",
    "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg"
)

// 멀티프로필
// id, image,
fun getDummyProfiles() = listOf(
    ProfilePreview(
        1,
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        "desc1",
        true
    ),
    ProfilePreview(
        2,
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        "desc2",
        false
    ),
    ProfilePreview(
        3,
        "https://cdn.pixabay.com/photo/2014/04/13/20/49/cat-323262__480.jpg",
        "desc3",
        false
    ),
    ProfilePreview(
        4,
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        "desc4",
        false
    )
)

// 생일인 친구
fun getBirthdayFriend() = listOf(
    Friend(
        12,
        "1643163512893324414",
        "플레임 소연",
        FriendPreview(
            2,
            "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
            "끝까지 열심히!!"
        )
    ),
    Friend(
        15,
        "1643163512893324415",
        "플레임 수연",
        FriendPreview(
            21,
            "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
            "안녕..."
        )
    )
)

// 친구 리스트
fun getDummyFriend() = listOf(
    Friend(
        12,
        "1643163512893324414",
        "플레임 소연",
        FriendPreview(
            2,
            "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
            "끝까지 열심히!!"
        )
    ),
    Friend(
        15,
        "1643163512893324415",
        "플레임 수연",
        FriendPreview(
            21,
            "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
            "안녕..."
        )
    ),
    Friend(
        12,
        "1643163512893324414",
        "플레임 소연",
        FriendPreview(
            2,
            "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
            "끝까지 열심히!!"
        )
    ),
    Friend(
        15,
        "1643163512893324415",
        "플레임 수연",
        FriendPreview(
            21,
            "https://cdn.pixabay.com/photo/2016/01/20/13/05/cat-1151519__480.jpg",
            "안녕..."
        )
    )
)

fun getDummyHiddenFriend() = listOf(
    Friend(
        12,
        "1643163512893324414",
        "플레임 소연",
        FriendPreview(
            2,
            "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
            "끝까지 열심히!!"
        )
    ),
    Friend(
        15,
        "1643163512893324415",
        "플레임 수연",
        FriendPreview(
            21,
            "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
            "안녕..."
        )
    )
)

fun getDummyFriendModel() = listOf(
    FriendModel(
        12,
        "박소연",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        "끝까지 열심히!!"
    ),
    FriendModel(
        13,
        "최수연",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        "안녕~"
    ),
    FriendModel(
        14,
        "김현국",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        "화아팅!!"
    ),
    FriendModel(
        15,
        "김다롬",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        "아자아자~"
    )
)
