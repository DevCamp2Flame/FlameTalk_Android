package com.sgs.devcamp2.flametalk_android.util

import okhttp3.Request

/**
 * @author 박소연
 * @created 2022/02/11
 * @desc Retrofit 관련 확장함수
 */

// 요청 헤더의 코드를 줄여줌
fun Request.addHeader(name: String, value: String): Request {
    return this.newBuilder().addHeader(name, value).build()
}
