package com.sgs.devcamp2.flametalk_android.network

import com.google.gson.Gson
import com.sgs.devcamp2.flametalk_android.FlameTalkApp
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.response.ErrorResponse
import com.sgs.devcamp2.flametalk_android.util.AuthUtil
import com.sgs.devcamp2.flametalk_android.util.addHeader
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/01/13
 * @updated 2022/01/20
 * @desc Header에 access token 자동 주입
 */

class NetworkInterceptor(
    private val userDAO: UserDAO,
    val tokenSupplier: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "ACCESS-TOKEN",
                tokenSupplier().also { Timber.d("ACCESS-TOKEN: $it") }.toString()
            )

        Timber.d("url -> ${request.url}")
        Timber.d("headers -> ${request.headers}")
        Timber.d("body -> ${request.body?.toBodyInfo()}")

        val response = chain.proceed(request)

        Timber.d(response.toString())
        Timber.d(JSONObject(response.peekBody(Long.MAX_VALUE).string()).toString(4))

        // 통일된 응답을 logging
        response.peekBody(Long.MAX_VALUE).string().let { JSONObject(it) }.let {

            val status = it["status"] as Int
            val message = it["message"] as String
            val data = it["data"] as JSONObject

            Timber.d("url -> $status")
            Timber.d("headers -> $message")
            Timber.d("body -> $data")
        }

        try {
            // 성공이 아니고 응답 body 비어있는 경우
            if (response.code !in (200..299) && response.body != null) {
                val responseBody = Gson().fromJson(
                    response.peekBody(Long.MAX_VALUE).string(),
                    ErrorResponse::class.java
                )

                when (responseBody.status) {
                    // access-token 만료, refresh-token 유효 => token 모두 갱신
                    307 -> {
                        tokenSupplier()?.let {
                            // TODO: hilt를 통해 authService를 주입받지 않고 토큰을 재요청 해야한다.
                            // AuthUtil().postRefreshToken(authService, userDAO)
                        }
                    }
                    // access-token 만료, refresh-token 만료 => 로그아웃
                    else -> {
                        Timber.d("$responseBody")
                        FlameTalkApp.currentActivity()?.let { AuthUtil().logout(it, userDAO) }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.d("NetworkInterceptor Exception ${e.message}")
        }

        Timber.d("response -> $response")

        return response
    }

    fun RequestBody.toBodyInfo(): String? {
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
