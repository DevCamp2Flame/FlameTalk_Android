package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat

/**
 * @author 김현국
 * @created 2022/02/01
 */
@Dao
interface ChatDao {

    //채팅 저장
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chat: Chat): Long
}
