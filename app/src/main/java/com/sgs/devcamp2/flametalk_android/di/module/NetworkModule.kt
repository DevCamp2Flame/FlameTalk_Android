package com.sgs.devcamp2.flametalk_android.di.module

import com.google.gson.GsonBuilder
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.*
import com.sgs.devcamp2.flametalk_android.network.NetworkInterceptor
import com.sgs.devcamp2.flametalk_android.network.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc Dagger+Hilt를 이용한 Network Module
 *       data -> source -> remote api들의 의존성 주입
 *
 *       NetworkInterceptor -> OkHttp 통신 객체 -> Retrofit 객체 -> 통신 api 객체 순으로 의존성을 주입
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkInterceptor(userPreferences: UserPreferences): NetworkInterceptor {
        /**통신 요청과 응답 시 NetworkInterceptor를 거친다
         userToken이 있는 경우 토큰을 주입한다.*/
        return NetworkInterceptor(userPreferences) {
            runBlocking(Dispatchers.IO) {
                userPreferences.user.first()?.accessToken
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp3Client(networkInterceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor) // OkHttp 객체에 interceptor를 주입한다.
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
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    /*REST API 통신에 사용되는 객체에 의존성 주입*/

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
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun provideFriendService(retrofit: Retrofit): FriendService {
        return retrofit.create(FriendService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRoomsService(retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenProfileApi(retrofit: Retrofit): OpenProfileApi {
        return retrofit.create(OpenProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceApi(retrofit: Retrofit): DeviceApi {
        return retrofit.create(DeviceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }
    @Provides
    @Singleton
    fun provideFriendApi(retrofit: Retrofit): FriendApi {
        return retrofit.create(FriendApi::class.java)
    }

    companion object {
        const val BASE_URL = "http://10.99.13.235:8080" // 테스트 전 PC IP 확인
    }
}
