package com.sgs.devcamp2.flametalk_android.data.model.friend

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 박소연
 * @created 2022/02/12
 * @created 2022/02/12
 * @desc RoomDB에 저장할 친구 프로필 정보. 검색에 이용한다.
 *
 * [FriendModel]
 * profileId         	String	친구의 프로필 id
 * nickname         	String	친구의 유저 닉네임
 * imageUrl         	String	친구의 프로필 이미지 url
 * description         	String	친구의 상태메세지
 */

@Entity(tableName = "friend")
data class FriendModel(
    @PrimaryKey
    @ColumnInfo(name = "profileId")
    val profileId: Long,
    @NonNull
    @ColumnInfo(name = "nickname")
    val nickname: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,
    @ColumnInfo(name = "description")
    val description: String?
)
