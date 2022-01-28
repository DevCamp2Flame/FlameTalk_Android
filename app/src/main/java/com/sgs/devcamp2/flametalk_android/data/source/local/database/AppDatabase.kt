package com.sgs.devcamp2.flametalk_android.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgs.devcamp2.flametalk_android.data.source.local.dao.ChatRoomListDao
import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList

/**
 * @author boris
 * @created 2022/01/27
 */
@Database(entities = [ChatRoomList::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chatRoomListDao(): ChatRoomListDao

    companion object {
        const val CHAT_ROOM_LIST_DB_NAME = "AppDatabase.db"
    }
}
