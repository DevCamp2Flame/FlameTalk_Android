package com.sgs.devcamp2.flametalk_android.data.dummy

import com.sgs.devcamp2.flametalk_android.data.model.ProfileHorizentalFeed
import com.sgs.devcamp2.flametalk_android.data.model.ProfileVerticalFeed
import com.sgs.devcamp2.flametalk_android.network.response.feed.Feed
import com.sgs.devcamp2.flametalk_android.network.response.feed.ProfileFeedResponse

/**
 * @author 박소연
 * @created 2022/02/03
 * @desc 프로필 피드 UI 테스트용 더미데이터
 *       통신 연결 후 없애야 한다
 */

// 프로필 피드 전체 데이터 (프로필, 배경)
fun getAllFeed() = arrayListOf(
    ProfileVerticalFeed(
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        true,
        "방금",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        true
    ),
    ProfileVerticalFeed(
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        false,
        "2분 전",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        false
    ),
    ProfileVerticalFeed(
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        true,
        "3일 전",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        false
    ),
    ProfileVerticalFeed(
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        false,
        "10일 전",
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        true
    )
)

// 나의 오픈채팅방 리스트
fun getProfileFeed() = arrayListOf(
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        false
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        true
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        true
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        false
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/11/14/13/06/kitty-2948404__480.jpg",
        false
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        true
    )
)

fun getBackgroundFeed() = arrayListOf(
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        true
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        false
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2018/03/26/02/05/cat-3261420__480.jpg",
        false
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg",
        true
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg",
        true
    ),
    ProfileHorizentalFeed(
        "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662__480.jpg", false
    )
)

fun getDummyFeed() = ProfileFeedResponse(
    200,
    "피드 리스트 조회 성공",
    ProfileFeedResponse.Data(
        1,
        "플레임",
        "https://flametalk-bucket.s3.ap-northeast-2.amazonaws.com/profile/flametalk_a_20222621172609.JPG",
        feeds = arrayListOf(
            Feed(
                1,
                "https://flametalk-bucket.s3.ap-northeast-2.amazonaws.com/profile/flametalk_a_20222621172609.JPG",
                true,
                false,
                "2022-01-14T10:52:39",
                "2022-01-19T10:52:39"
            ),
            Feed(
                1,
                "https://flametalk-bucket.s3.ap-northeast-2.amazonaws.com/profile/flametalk_a_20222621172609.JPG",
                true,
                false,
                "2022-01-14T10:52:39",
                "2022-01-19T10:52:39"
            )
        )
    )
)
