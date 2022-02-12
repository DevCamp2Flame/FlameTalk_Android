package com.sgs.devcamp2.flametalk_android.data.model.friend

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/31
 * @created 2022/02/06
 * @desc 프로필 정보
 *
 * [Friend]
 * friendId	            String	친구 관계 id
 * userId	            String	친구의 유저 id
 * nickname         	String	친구의 유저 닉네임
 * preview	            Object	친구 유저 프로필. preview 참고
 */

@Keep
@Entity(tableName = "friends")
data class Friend(
    @PrimaryKey
    @SerializedName("friendId")
    @ColumnInfo(name = "friend_id")
    val friendId: Long,
    @SerializedName("userId")
    @ColumnInfo(name = "user_id")
    val userId: String,
    @SerializedName("nickname")
    @ColumnInfo(name = "nickname")
    val nickname: String,
    @SerializedName("preview")
    @ColumnInfo(name = "preview")
    val preview: FriendPreview,
)