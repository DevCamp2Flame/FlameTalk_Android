package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.services.WebSocketListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Singleton

/**
 * @author 김현국
 * @created 2022/01/31
 * Websocket관련 Singleton module
 */
@Module
@InstallIn(SingletonComponent::class)
class WebSocketModule {

    @Provides
    @Singleton
    fun provideOkHttp3WebSocketClient(okHttpClient: OkHttpClient): OkHttpWebSocketClient {
        return OkHttpWebSocketClient(okHttpClient)
    }
    @Provides
    @Singleton
    fun provideWebSocketClient(okHttpWebSocketClient: OkHttpWebSocketClient): StompClient {
        return StompClient(okHttpWebSocketClient)
    }

    @Provides
    @Singleton
    fun provideNewWebsocket(): WebSocketListener {
        return WebSocketListener()
    }

    @Provides
    @Singleton
    fun provideRequest(): Request {
        return Request.Builder().url("ws://10.99.30.180:8086/presence").build()
    }
}
