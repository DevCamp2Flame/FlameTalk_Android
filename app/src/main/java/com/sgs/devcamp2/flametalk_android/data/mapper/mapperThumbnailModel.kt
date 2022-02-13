package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import java.util.*

/**
 * @author 김현국
 * @created 2022/02/08
 * Thumbnail을 Local database에 저장할 수 있도록 Thumbnail data model로 변환하는 Mapper 입니다.
 */

fun mapperToThumbnail(roomId: String, thumbnail: String): Thumbnail {

    return Thumbnail(0, roomId, thumbnail)
}
