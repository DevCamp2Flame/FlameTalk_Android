package com.sgs.devcamp2.flametalk_android.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Singleton

/**
 * @author boris
 * @created 2022/01/19
 */
@InstallIn(SingletonComponent::class)
@Module
class WebSocketListener : WebSocketListener() {
    @Provides
    @Singleton
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

    }
    @Provides
    @Singleton
    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("Socket", "Receiving text : $text")
    }
    @Provides
    @Singleton
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("Socket", "Receiving bytes : $bytes")
    }
    @Provides
    @Singleton
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }
    @Provides
    @Singleton
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("Socket","Closing : $code / $reason")
        webSocket.cancel()
    }
    @Provides
    @Singleton
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Socket", "Error : " + t.message)
    }
}
