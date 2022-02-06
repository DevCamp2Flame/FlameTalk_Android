package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat

/**
 * @author boris
 * @created 2022/02/01
 */
@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chat: Chat): Long
}
