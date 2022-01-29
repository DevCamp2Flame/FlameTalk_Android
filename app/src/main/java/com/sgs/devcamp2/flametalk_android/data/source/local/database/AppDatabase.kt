package com.sgs.devcamp2.flametalk_android.data.source.local.database

import com.sgs.devcamp2.flametalk_android.data.source.local.dao.ChatRoomDao

/**
 * @author boris
 * @created 2022/01/29
 * dao directory에서 Dao 폴더들의 interface
 * 추가적인 dao 들을 여기에 선언
 */
interface AppDatabase {
    fun chatRoomDao(): ChatRoomDao
}
