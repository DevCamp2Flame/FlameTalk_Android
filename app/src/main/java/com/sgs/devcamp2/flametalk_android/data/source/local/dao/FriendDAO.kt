package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.*
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend

/**
 * @author 박소연
 * @created 2022/02/11
 * @updated 2022/02/11
 * @desc 친구 검색을 위한 친구 리스트를 RoomDB에 저장, 조회, 삭제
 */

@Dao
interface FriendDAO {
    // 친구 리스트 로컬에 저장. 기존에 있을 경우 갱신
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFriends(friends: List<Friend>)

    // 친구 전체 조회
    @Query("SELECT * FROM friends")
    fun getAllFriends(userId: String): List<Friend>
}
