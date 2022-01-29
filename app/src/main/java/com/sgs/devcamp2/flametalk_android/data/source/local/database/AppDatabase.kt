package com.sgs.devcamp2.flametalk_android.data.source.local.database

import com.sgs.devcamp2.flametalk_android.data.source.local.dao.ChatRoomDao

/**
 * @author boris
 * @created 2022/01/29
 */
interface AppDatabase {
    fun chatRoomDao(): ChatRoomDao
}
