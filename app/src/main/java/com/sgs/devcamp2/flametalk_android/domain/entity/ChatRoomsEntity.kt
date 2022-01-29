package com.sgs.devcamp2.flametalk_android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/01/28
 * Room,Retrofit 등과 연관되지 않는 순수 DATA CLASS 이며,
 * ui에 사용될 실제 필요한 데이터만 가지고 있다.
 * loca db 데이터는 data -> model 에 있으므로 , model를 entity로 변환하는 mapper가 필요함
 * entity로 이름을 지었지만, data class들을 domin layer 내부에 model directory를 생성해서 넣어놔도 좋을 듯
 * 아래 ui entity와는 어울리지가 않음 .
 */
@Parcelize
data class ChatRoomsEntity(
    val room_id: String,
    val host_id: String,
    val title: String,
    val is_open: Boolean,
    val user_size: Int
) : Parcelable
