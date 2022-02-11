package com.sgs.devcamp2.flametalk_android.data.model.friend

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
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
@Entity(tableName = "friends_preview")
data class FriendPreview(
    @SerializedName("profileId")
    @ColumnInfo(name = "profile_id")
    val profileId: Long,
    @SerializedName("imageUrl")
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String
)
