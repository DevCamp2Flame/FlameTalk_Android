package com.sgs.devcamp2.flametalk_android.data.model.friend

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 프로필 기본 정보
 *
 * [FriendPreview]
 * profileId	Long	친구 유저 프로필 id
 * imageUrl	    String	친구 유저 프로필 사진 S3 URL
 * description	String	친구 유저 프로필 상태 메세지
 */

@Keep
@Entity(tableName = "preview")
data class FriendPreview(
    @PrimaryKey
    @SerializedName("profileId")
    @ColumnInfo(name = "profileId")
    val profileId: Long,
    @SerializedName("imageUrl")
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String
)
