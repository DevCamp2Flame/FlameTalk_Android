package com.sgs.devcamp2.flametalk_android.data.model.auth

/**
 * @author boris
 * @created 2022/02/18
 */
data class AuthReq(
    val email: String,
    val password: String,
    val nickname: String,
    val phoneNumber: String,
    val birthday: String,
    val social: String,
    val region: String,
    val language: String,
    val deviceId: String
)
