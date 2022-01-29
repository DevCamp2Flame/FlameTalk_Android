package com.sgs.devcamp2.flametalk_android.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom

/**
 * @author boris
 * @created 2022/01/27
 */
@Database(entities = [ChatRoom::class], version = 1, exportSchema = false)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {

    companion object {
        const val DB_NAME = "AppDatabase.db"
    }
}
