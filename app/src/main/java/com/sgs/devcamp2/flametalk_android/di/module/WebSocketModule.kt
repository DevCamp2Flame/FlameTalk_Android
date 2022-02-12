package com.sgs.devcamp2.flametalk_android.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Singleton

/**
 * @author boris
 * @created 2022/01/31
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
}
