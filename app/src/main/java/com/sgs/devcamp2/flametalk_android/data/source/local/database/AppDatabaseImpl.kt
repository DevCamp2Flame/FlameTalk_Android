package com.sgs.devcamp2.flametalk_android.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoomUpdate
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail

/**
 * @author 김현국
 * @created 2022/01/27
 * AppDatabase interface를 의존성 주입을 받아서 AppDatabase 생성
 */
@Database(entities = [ChatRoom::class, Chat::class, Thumbnail::class, ChatRoomUpdate::class], version = 1, exportSchema = false)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {

    companion object {
        const val DB_NAME = "AppDatabase.db"
    }
}
