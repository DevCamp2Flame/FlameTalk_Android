package com.sgs.devcamp2.flametalk_android.domain.dummy

import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.ProfilePreview

/**
 * @author 박소연
 * @created 2022/01/14
 * @desc 1번째 탭 친구 리스트 UI 테스트용 더미데이터
 *       통신 연결 후 없애야 한다
 */

// 유저 정보 1개
fun getDummyUser() = ProfilePreview(
    "박소연",
    "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png",
    "아자아자"
)

// 멀티프로필
fun getMultiProfile() = listOf(
    ProfilePreview(
        "박소연2",
        "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png",
        "아자아자"
    ),
    ProfilePreview(
        "박소연3",
        "https://user-images.githubusercontent.com/43838030/114815363-01ee3680-9df1-11eb-8154-df01dca81679.png",
        "아자아자"
    ),
    ProfilePreview(
        "박소연4",
        "https://user-images.githubusercontent.com/43838030/114815363-01ee3680-9df1-11eb-8154-df01dca81679.png",
        "아자아자"
    ),
    ProfilePreview(
        "박소연5",
        "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png",
        "아자아자"
    )
)

// 생일인 친구
fun getBirthdayFriend() = listOf(
    ProfilePreview("최수연6", null, "소연 짱 팬222"),
    ProfilePreview("김다롬7", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", null)
)

// 친구 리스트
fun getDummyFriend() = listOf(
    ProfilePreview("최수연", null, "소연 짱 팬"),
    ProfilePreview("김다롬", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", null),
    ProfilePreview("최수연2", null, "소연 짱 팬222"),
    ProfilePreview("김다롬2", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", null),
    ProfilePreview("최수연3", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", "소연 짱 팬333"),
    ProfilePreview("최수연4", null, "소연 짱 팬"),
    ProfilePreview("김다롬5", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", null),
    ProfilePreview("최수연6", null, "소연 짱 팬222"),
    ProfilePreview("김다롬7", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", null),
    ProfilePreview("최수연8", "https://user-images.githubusercontent.com/43838030/114301612-9b230180-9b00-11eb-889e-dd7981d2a5ae.png", "소연 짱 팬333")
)
