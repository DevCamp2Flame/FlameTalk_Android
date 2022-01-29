package com.sgs.devcamp2.flametalk_android.network

import com.sgs.devcamp2.flametalk_android.util.addHeader
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import timber.log.Timber
import java.io.IOException

/**
 * @author 박소연
 * @created 2022/01/13
 * @updated 2022/01/20
 * @desc Header에 access token을 자동 주입
 */

class NetworkInterceptor(
    val tokenSupplier: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val TAG: String = "로그"
        val request = chain.request().newBuilder().addHeader("Content-Type", "application/json")
            .build()

//          val request = chain.request().addHeader("Content-Type", "application/json")
//        val request = chain.request()
//            .addHeader("x-access-token", tokenSupplier().also { Timber.d("token: $it") }.toString())
//            .addHeader("accept", "application/json")
//            .addHeader("Content-Type", "application/json")

        //    Timber.d("url -> ${request.url}")
        //  Timber.d("headers -> ${request.headers}")
        // Timber.d("body -> ${request.body?.toBodyInfo()}")

        val response = chain.proceed(request)

// Timber.d(response.toString())
        // Timber.d(JSONObject(response.peekBody(Long.MAX_VALUE).string()).toString(4))

        // 서버에서 response 형식을 통일하지 않으면 못 씀
        // response.peekBody(Long.MAX_VALUE).string().let { JSONObject(it) }.let {
            /*val isSuccess = it["isSuccess"] as Boolean
            val code = it["code"] as Int
            val message = it["message"] as String*/

        // if (!isSuccess) throw NetworkError(message)
        // }

        //   Timber.d("response -> $response")

        return response
    }

    private fun RequestBody.toBodyInfo(): String? {
        return try {
            val copy = this
            val buffer = Buffer()
            copy.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Throwable) {
            null
        }
    }
}

class NetworkError(message: String, val code: Int) : IOException(message)
