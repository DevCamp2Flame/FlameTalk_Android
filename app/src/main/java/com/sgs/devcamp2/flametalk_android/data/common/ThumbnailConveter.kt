package com.sgs.devcamp2.flametalk_android.data.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import java.lang.reflect.Type
import java.util.*

/**
 * @author 김현국
 * @created 2022/02/11
 */
@TypeConverter
fun stringToThumbnailList(data: String?): List<Thumbnail?>? {
    if (data == null) {
        return Collections.emptyList()
    }
    val listType: Type = object : TypeToken<List<Thumbnail?>?>() {}.type
    return Gson().fromJson<List<Thumbnail?>?>(data, listType)
}
@TypeConverter
fun thumbnailListToString(listData: List<Thumbnail?>?): String? {
    return Gson().toJson(listData)
}
