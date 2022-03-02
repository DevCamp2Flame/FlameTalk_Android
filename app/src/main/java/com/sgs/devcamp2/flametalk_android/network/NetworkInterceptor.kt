package com.sgs.devcamp2.flametalk_android.network

import com.google.gson.Gson
import com.sgs.devcamp2.flametalk_android.FlameTalkApp
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.network.response.ErrorResponse
import com.sgs.devcamp2.flametalk_android.util.AuthUtil
import com.sgs.devcamp2.flametalk_android.util.addHeader
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import okio.IOException
import org.hildan.krossbow.stomp.WebSocketConnectionException
import org.json.JSONObject
import timber.log.Timber
import java.net.ConnectException

/**
 * @author 박소연
 * @created 2022/01/13
 * @updated 2022/01/20
 * @desc Network와 가장 가까운 위지에서 통신을 낚아채 response와 request의 로그를 확인
 *       Header에 access token 자동 주입
 */

class NetworkInterceptor(
    private val userPreferences: UserPreferences,
    val tokenSupplier: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        lateinit var response: Response
        // Content-Type 정보와 파라미터로 받은 token을 API 요청 시 request header에 자동 주입
        val request = chain.request()
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "ACCESS-TOKEN",
                tokenSupplier().also { Timber.d("ACCESS-TOKEN: $it") }.toString()
            )

        //   .addHeader("userId", "1644502326105613328")

        // Request를 로그로 기록
        Timber.d("url -> ${request.url}")
        Timber.d("headers -> ${request.headers}")
        Timber.d("body -> ${request.body?.toBodyInfo()}")

        try {
            response = chain.proceed(request)
        } catch (e: ConnectException) {
            // Timber.d("exception -> $e")
            var jsonObject: JSONObject = JSONObject()
            jsonObject.put("status", 600)
            jsonObject.put("message", "error")
            var responseBody = ResponseBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            response = Response.Builder().code(600).request(request).protocol(Protocol.HTTP_1_1).message("error").body(responseBody).build()
            Timber.d("response -> $response")
        }

        if (!response.isSuccessful) {
            Timber.d("Code ${response.code}")
        }

        // Body로 넘어온 Resonse를 indent 처리하여 로그로 기록
        Timber.d(response.toString())
        // Timber.d(JSONObject(response.peekBody(Long.MAX_VALUE).string()).toString(4))

        try {
            // 실패는 아니지만 응답 body 비어있는 경우
            if (response.code !in (200..299) && response.body != null) {
                val responseBody = Gson().fromJson(
                    response.peekBody(Long.MAX_VALUE).string(),
                    ErrorResponse::class.java
                )

                when (responseBody.status) {
                    // access-token 만료, refresh-token 유효 => token 모두 갱신
                    302 -> {
                        tokenSupplier()?.let {
                            val request = chain.request()
                                .addHeader(
                                    "ACCESS-TOKEN",
                                    userPreferences.getAccessToken().toString()
                                )
                                .addHeader(
                                    "REFRESH-TOKEN",
                                    userPreferences.getRefreshToken().toString()
                                )
                            // TODO: response를 받으면 request를 보내는 retrofit 객체를 새로 생성하여 요청해야 함
                            response = chain.proceed(request.newBuilder().build())

                            Timber.d("RenewToken Response: $response")
                        }
                    }
                    // access-token 만료, refresh-token 만료 => 로그아웃
                    307 -> {
                        Timber.d("$responseBody")
                        FlameTalkApp.currentActivity()
                            ?.let { AuthUtil().logout(it, userPreferences) }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.d("NetworkInterceptor Exception ${e.message}")
        }

        Timber.d("response -> $response")

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
