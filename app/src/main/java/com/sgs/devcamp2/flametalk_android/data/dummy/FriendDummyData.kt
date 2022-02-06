package com.sgs.devcamp2.flametalk_android.data.dummy

import com.sgs.devcamp2.flametalk_android.data.model.ProfileDummyPreview

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
fun getMultiProfile() = listOf(
    ProfileDummyPreview(
        1,
        2,
        "박소연2",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        "아자아자",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    ),
    ProfileDummyPreview(
        1,
        3,
        "박소연3",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        "아자아자",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
    ),
    ProfileDummyPreview(
        1,
        4,
        "박소연4",
        "https://cdn.pixabay.com/photo/2014/04/13/20/49/cat-323262__480.jpg",
        "아자아자",
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
    ),
    ProfileDummyPreview(
        1,
        5,
        "박소연5",
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        "아자아자",
        null
    )
)

// 생일인 친구
fun getBirthdayFriend() = listOf(
    ProfileDummyPreview(2, 6, "최수연6", null, "소연 짱 팬222", null),
    ProfileDummyPreview(
        3,
        7,
        "김다롬7",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        null,
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
    )
)

// 친구 리스트
fun getDummyFriend() = listOf(
    ProfileDummyPreview(4, 8, "최수연", null, "소연 짱 팬", null),
    ProfileDummyPreview(
        5,
        9,
        "김다롬",
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        null,
        null
    ),
    ProfileDummyPreview(
        6,
        10,
        "최수연2",
        null,
        "소연 짱 팬222",
        null
    ),
    ProfileDummyPreview(
        7,
        11,
        "김다롬2",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        null,
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
    ),
    ProfileDummyPreview(
        8,
        12,
        "최수연3",
        "https://cdn.pixabay.com/photo/2016/01/20/13/05/cat-1151519__480.jpg",
        "소연 짱 팬333",
        null
    ),
    ProfileDummyPreview(
        9,
        13,
        "최수연4",
        null,
        "소연 짱 팬",
        null
    ),
    ProfileDummyPreview(
        10,
        14,
        "김다롬5",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        null,
        null
    ),
    ProfileDummyPreview(
        11,
        15,
        "최수연6",
        null,
        "소연 짱 팬222",
        null
    ),
    ProfileDummyPreview(
        12,
        16,
        "김다롬7",
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        null,
        null
    ),
    ProfileDummyPreview(
        13,
        17,
        "최수연8",
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        "소연 짱 팬333",
        null
    )
)
