package com.sgs.devcamp2.flametalk_android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/02/03
 */
@Parcelize
data class ProfileEntity(
    val imageUrl: String,
    val description: String
) : Parcelable
