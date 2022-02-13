package com.sgs.devcamp2.flametalk_android.data.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author 김현국
 * @created 2022/02/11
 */
@TypeConverter
fun formStringList(thumbnailList: List<String>): String {
    val type = object : TypeToken<List<String>>() {}.type
    return Gson().toJson(thumbnailList, type)
}
@TypeConverter
fun formJsonToList(json: String): List<String> {
    val type = object : TypeToken<List<String>>() {}.type
    return Gson().fromJson(json, type)
}
