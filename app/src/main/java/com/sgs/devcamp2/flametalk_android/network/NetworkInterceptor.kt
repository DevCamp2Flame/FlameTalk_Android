package com.sgs.devcamp2.flametalk_android.network

import com.sgs.devcamp2.flametalk_android.util.addHeader
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException

class NetworkInterceptor (
    val tokenSupplier: () -> String?
    ): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .addHeader("x-access-token", tokenSupplier().also { Timber.d("token: $it") }.toString())
         .addHeader("accept", "application/json")
         .addHeader("Content-Type", "application/json")

        Timber.d("url -> ${request.url}")
        Timber.d("headers -> ${request.headers}")
        Timber.d("body -> ${request.body?.toBodyInfo()}")

        val response = chain.proceed(request)

        Timber.d(response.toString())
        Timber.d(JSONObject(response.peekBody(Long.MAX_VALUE).string()).toString(4))

         response.peekBody(Long.MAX_VALUE).string().let { JSONObject(it) }.let {
             val isSuccess = it["isSuccess"] as Boolean
             val code = it["code"] as Int
             val message = it["message"] as String

             if (!isSuccess) throw NetworkError(message, code)
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