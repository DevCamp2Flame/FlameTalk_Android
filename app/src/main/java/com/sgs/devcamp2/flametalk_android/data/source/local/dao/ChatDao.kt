package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat

/**
 * @author boris
 * @created 2022/02/01
 */
@Dao
interface ChatDao {
    @Insert
    suspend fun insert(chat: Chat)

}
