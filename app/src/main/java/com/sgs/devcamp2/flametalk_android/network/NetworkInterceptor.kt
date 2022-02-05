package com.sgs.devcamp2.flametalk_android.network

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
 * @desc Header에 access token을 자동 주입
 */

class NetworkInterceptor(
    val tokenSupplier: () -> String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Content-Type", "application/json")
            .addHeader(
                "ACCESS-TOKEN",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNjQzOTY4NTg0ODI4ODE5NzQyIiwibmlja25hbWUiOiJhZG1pbiIsInN0YXR1cyI6IlJPTEVfR1VFU1QiLCJkZXZpY2VJZCI6ImJmN2U5OGViY2EwNzdmNWUiLCJpYXQiOjE2NDM5NzA1NDQsImV4cCI6MTY0NjU2MjU0NH0.Hpe7tn6aeocWBTB_8ZdWa4whD2lF2kc8CaAqYcp4_gI"
            )
            .build()

        Timber.d("url -> ${request.url}")
        Timber.d("headers -> ${request.headers}")
        Timber.d("body -> ${request.body?.toBodyInfo()}")

        val response = chain.proceed(request)

        Timber.d("response -> ${JSONObject(response.peekBody(Long.MAX_VALUE).string()).toString(4)}")

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
