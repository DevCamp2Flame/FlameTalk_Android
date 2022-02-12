package com.sgs.devcamp2.flametalk_android.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.source.local.dao.FriendDAO

/**
 * @author boris
 * @created 2022/01/27
 * AppDatabase interface를 의존성 주입을 받아서 AppDatabase 생성
 */
@Database(entities = [ChatRoom::class, Friend::class], version = 1, exportSchema = false)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {
    // [소연] 친구 정보와 관련된 DAO
    abstract fun friendDAO(): FriendDAO

    companion object {
        const val DB_NAME = "AppDatabase.db"
    }
}
