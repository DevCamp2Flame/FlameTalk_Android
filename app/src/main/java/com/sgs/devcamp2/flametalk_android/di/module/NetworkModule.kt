package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.data.source.remote.api.ChatRoomsApi
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.InviteRoomApi
import com.sgs.devcamp2.flametalk_android.network.NetworkInterceptor
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.service.FileService
import com.sgs.devcamp2.flametalk_android.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc Dagger+Hilt를 이용한 Network Module
 */

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkInterceptor(userDAO: UserDAO): NetworkInterceptor {
        return NetworkInterceptor {
            runBlocking(Dispatchers.IO) {
                userDAO.user.first()?.accessToken
                userDAO.user.first()?.refreshToken
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp3Client(networkInterceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideFileService(retrofit: Retrofit): FileService {
        return retrofit.create(FileService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRoomsService(retrofit: Retrofit): ChatRoomsApi {
        return retrofit.create(ChatRoomsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInviteRoomApi(retrofit: Retrofit): InviteRoomApi {
        return retrofit.create(InviteRoomApi::class.java)
    }

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080" // emulator
        // const val BASE_URL = "http://10.99.30.180:8080" // physical device
    }
}
