package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import java.util.*

/**
 * @author boris
 * @created 2022/02/08
 */

fun mapperToThumbnail(roomId: String, thumbnail: String): Thumbnail {

    return Thumbnail(thumbnail, roomId, thumbnail)
}
