package com.sgs.devcamp2.flametalk_android.network


import com.google.gson.Gson
import com.sgs.devcamp2.flametalk_android.FlameTalkApp
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.response.ErrorResponse
import com.sgs.devcamp2.flametalk_android.util.AuthUtil
import com.sgs.devcamp2.flametalk_android.util.addHeader
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okio.Buffer
import okio.IOException
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

        var response = chain.proceed(request)


        if (!response.isSuccessful) {
            Timber.d("Code ${response.code}")
        }

        Timber.d(response.toString())
        //Timber.d(JSONObject(response.peekBody(Long.MAX_VALUE).string()).toString(4))

        try {
            // 성공이 아니고 응답 body 비어있는 경우
            if (response.code !in (200..299) && response.body != null) {
                val responseBody = Gson().fromJson(
                    response.peekBody(Long.MAX_VALUE).string(),
                    ErrorResponse::class.java
                )

                when (responseBody.status) {
                    // access-token 만료, refresh-token 유효 => token 모두 갱신
                    302 -> {
                        // TODO: request를 보내는 retrofit 객체를 새로 생성해야 함
                        tokenSupplier()?.let {
                            val request = chain.request()
                                .addHeader("ACCESS-TOKEN", userDAO.getAccessToken().toString())
                                .addHeader("REFRESH-TOKEN", userDAO.getRefreshToken().toString())
                            response = chain.proceed(request.newBuilder().build())

                            // TODO: response를 받으면 pickBody를 codeGen 방식으로 다시 해야 함
                            Timber.d("RenewToken Response: $response")
                        }
                    }
                    // access-token 만료, refresh-token 만료 => 로그아웃
                    307 -> {
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
