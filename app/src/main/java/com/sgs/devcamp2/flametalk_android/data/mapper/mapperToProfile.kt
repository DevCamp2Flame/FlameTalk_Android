package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile.Profiles
import com.sgs.devcamp2.flametalk_android.domain.entity.ProfileEntity

/**
 * @author boris
 * @created 2022/02/03
 */

fun mapperToProfileEntity(profiles: Profiles): ProfileEntity {
    return ProfileEntity(profiles.imageUrl, profiles.description)
}
