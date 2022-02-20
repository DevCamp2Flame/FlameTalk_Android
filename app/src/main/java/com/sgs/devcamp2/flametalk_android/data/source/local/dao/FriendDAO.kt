package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.*
import com.sgs.devcamp2.flametalk_android.domain.entity.FriendModel
import kotlinx.coroutines.flow.Flow

/**
 * @author 박소연
 * @created 2022/02/11
 * @updated 2022/02/11
 * @desc 검색을 위한 친구 프로필 정보에 접근하는 객체.
 *       친구 검색을 위한 친구 리스트를 RoomDB에 저장, 조회
 */

@Dao
interface FriendDAO {
    // 친구 리스트 로컬에 저장. 기존에 있을 경우 갱신
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFriends(friend: List<FriendModel>)

    // 친구 전체 조회
    @Query("SELECT * FROM friend")
    fun getAllFriends(): Flow<List<FriendModel>>

    @Query("SELECT imageUrl FROM friend WHERE userId = :userId")
    fun getFriendImageUrl(userId: String): String
}
