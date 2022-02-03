package com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile

/**
 * @author boris
 * @created 2022/02/03
 */

data class CreateOpenChatProfileRes(
    val userId: String,
    val profiles: List<Profiles>
)

data class Profiles(
    val id: Int,
    val imageUrl: String,
    val description: String,
    val isDefault: Boolean
)
